import { PublicKey } from '@solana/web3.js';
import { NATIVE_MINT } from '@solana/spl-token';
import axios from 'axios';
import { API_URLS, ApiSwapV1Out } from '@raydium-io/raydium-sdk-v2';
import logger from './winston';
import { TokenInfo } from '../components/interface';
import { LIQUIDITY_SHORTAGE_MAX_GAP, SWAP_PRICE_IMPACT_PCT } from '../components/constant';

const txVersion: string = 'V0';

export async function getRaydiumQuote(inputMint: string, outputMint: string, amount: number, slippageBps: number, tgUserId: string, tokenPriceMap: any): Promise<{ quote?: ApiSwapV1Out; message?: string, error: number }> {
  let slippage = 500;
  if (slippageBps !== -1) {
    slippage = slippageBps;
  }
  try {
    const { data: swapResponse } = await axios.get<ApiSwapV1Out>(`${API_URLS.SWAP_HOST}/compute/swap-base-in?inputMint=${inputMint}&outputMint=${outputMint}&amount=${amount}&slippageBps=${slippage}&txVersion=${txVersion}`);
    if (!swapResponse.success) {
      return { error: -1 };
    }
    
    const inputMintInfo = tokenPriceMap[inputMint];
    const outputMintInfo = tokenPriceMap[outputMint];
    let errorMessage = 'The slippage is too large';
    if (inputMintInfo !== undefined && outputMintInfo !== undefined) {
      const inputInfo = inputMintInfo as TokenInfo;
      const outputInfo = outputMintInfo as TokenInfo;
      const inputValue = Number.parseInt(swapResponse.data.inputAmount) / Math.pow(10, inputInfo.decimals) * Number.parseFloat(inputInfo.price);
      const outputValue = Number.parseInt(swapResponse.data.outputAmount) / Math.pow(10, outputInfo.decimals) * Number.parseFloat(outputInfo.price);
      const rate = (Math.abs(inputValue - outputValue) / (inputValue === 0 ? outputValue : inputValue)) * 1000;
      if (rate > (slippageBps + LIQUIDITY_SHORTAGE_MAX_GAP)) {
        errorMessage = `The slippage is too large; the current amount can only be exchanged for ${Number.parseInt(swapResponse.data.outputAmount) / Math.pow(10, outputInfo.decimals)} token.`;
        return { error: -2, message: errorMessage };
      }
    } else {
      if (swapResponse.data.priceImpactPct) {
        if (swapResponse.data.priceImpactPct > SWAP_PRICE_IMPACT_PCT) {
          return { error: -2, message: errorMessage };
        }
      }
    }  
    return { quote: swapResponse, error: 0 };
  } catch (error) {
    return { error: -1 };
  }
}

export async function getRaydiumSwapObj(inputMint: string, inputMintAccount: PublicKey, outputMint: string, outMintAccount: PublicKey, ownnerAddress: string, quote: ApiSwapV1Out, tgUserId: string): Promise<{ swapTransactionBuf?: Buffer; error: number }> {
  try {
    const [isInputSol, isOutputSol] = [inputMint === NATIVE_MINT.toBase58(), outputMint === NATIVE_MINT.toBase58()];
    const { data: swapTransactions } = await axios.post<{
      id: string
      version: string
      success: boolean
      data: { transaction: string }[]
    }>(`${API_URLS.SWAP_HOST}/transaction/swap-base-in`, {
      computeUnitPriceMicroLamports: '100000',
      swapResponse: quote,
      txVersion,
      wallet: ownnerAddress,
      wrapSol: isInputSol,
      unwrapSol: isOutputSol, // true means output mint receive sol, false means output mint received wsol
      inputAccount: isInputSol ? undefined : inputMintAccount.toBase58(),
      outputAccount: isOutputSol ? undefined : outMintAccount.toBase58(),
    });
    if (!swapTransactions.success) {
      return { error: -1 };
    }
    const allTxBuf = swapTransactions.data.map((tx) => Buffer.from(tx.transaction, 'base64'));
    if (allTxBuf.length !== 1) {
      return { error: -1 };
    }
    return { swapTransactionBuf: allTxBuf[0], error: 0 };
  } catch (error) {
    logger.error(`raydium Error swap: ${error}`, { tgUserId });
    return { error: -1 };
  }
}
