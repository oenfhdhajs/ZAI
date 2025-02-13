import logger from './winston';
import {
  QuoteGetRequest,
  QuoteResponse,
  createJupiterApiClient,
} from '../generated/index';
import { TokenInfo } from '../components/interface';
import { LIQUIDITY_SHORTAGE_MAX_GAP, SWAP_PRICE_IMPACT_PCT } from '../components/constant';

const jupiterQuoteApi = createJupiterApiClient();

export async function getJupiterQuote(inputMint: string, outputMint: string, amount: number, slippageBps: number, tgUserId: string, tokenPriceMap: any): Promise<{ quote?: QuoteResponse; message?: string; error: number }> {
  let params: QuoteGetRequest;
  if (slippageBps === -1) {
    params = {
      inputMint: inputMint,
      outputMint: outputMint,
      amount: amount,
      autoSlippage: true,
      maxAutoSlippageBps: 2000, 
    };
  } else {
    if (slippageBps === null || slippageBps === undefined) {
      slippageBps = 500;
    }
    if (slippageBps > 2000) {
      slippageBps = 2000;
    }
    params = {
      inputMint: inputMint,
      outputMint: outputMint,
      amount: amount,
      slippageBps: slippageBps,
      restrictIntermediateTokens: true,
    };
  }
  // params.restrictIntermediateTokens = true;
  params.dexes = ['Raydium CP', 'Raydium CLMM', 'Raydium'];
  // params.onlyDirectRoutes = true;
  params.restrictIntermediateTokens = true;

  const params2 = { ...params };
  params2.dexes = ['Raydium CP', 'Raydium CLMM', 'Raydium', 'Orca V2', 'Orca V1', 'Phoenix', 'SolFi', 'Meteora', 'Meteora DLMM', 'Lifinity V2', 'Lifinity V1', 'Pump.fun'];

  // get quote  
  try {
    const [results1, results2] = await Promise.allSettled([
      jupiterQuoteApi.quoteGet(params),
      jupiterQuoteApi.quoteGet(params2),
    ]);
    
    const quote1 = results1.status === 'fulfilled' ? results1.value : null;
    const quote2 = results2.status === 'fulfilled' ? results2.value : null;

    let resultQuote;
    if (quote1 && quote2) {
      resultQuote = quote1;
    } else if (quote1) {
      resultQuote = quote1;
    } else if (quote2) {
      resultQuote = quote2;
    } else {
      return { error: -1 };
    }

    const inputMintInfo = tokenPriceMap[inputMint];
    const outputMintInfo = tokenPriceMap[outputMint];
    
    let errorMessage = 'The slippage is too large';
    if (inputMintInfo !== undefined && outputMintInfo !== undefined) {
      const inputInfo = inputMintInfo as TokenInfo;
      const outputInfo = outputMintInfo as TokenInfo;
      const inputValue = Number.parseInt(resultQuote.inAmount) / Math.pow(10, inputInfo.decimals) * Number.parseFloat(inputInfo.price);
      const outputValue = Number.parseInt(resultQuote.outAmount) / Math.pow(10, outputInfo.decimals) * Number.parseFloat(outputInfo.price);
      const rate = (Math.abs(inputValue - outputValue) / (inputValue === 0 ? outputValue : inputValue)) * 1000;
      if (rate > (slippageBps + LIQUIDITY_SHORTAGE_MAX_GAP)) {
        errorMessage = `The slippage is too large; the current amount can only be exchanged for ${Number.parseInt(resultQuote.outAmount) / Math.pow(10, outputInfo.decimals)} token.`;
        return { error: -2, message: errorMessage };
      }
    } else {
      if (resultQuote.priceImpactPct) {
        if (Number.parseFloat(resultQuote.priceImpactPct) > SWAP_PRICE_IMPACT_PCT) {
          return { error: -2, message: errorMessage };
        }
      }
    }  
    return { quote: resultQuote, error: 0 };
  } catch (error) {
    logger.info('quote request error', { params });    
  }
  return { error: -1 };
}

export async function getJupiterSwapObj(ownnerAddress: string, quote: QuoteResponse, tgUserId: string): Promise<{ swapTransactionBuf?: Buffer; error: number }> {
  try {
    const swapObj = await jupiterQuoteApi.swapPost({
      swapRequest: {
        quoteResponse: quote,
        userPublicKey: ownnerAddress,
      },
    });
    const swapTransactionBuf = Buffer.from(swapObj.swapTransaction, 'base64');
    return { swapTransactionBuf, error: 0 };
  } catch (error) {
    logger.error(`jupiter Error swap attempts: ${error}`, { tgUserId });
    return { error: -1 };
  }
}