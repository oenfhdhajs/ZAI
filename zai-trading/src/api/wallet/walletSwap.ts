import express from 'express';
import { Connection, PublicKey, ComputeBudgetProgram, SystemProgram, VersionedTransaction, AddressLookupTableAccount, TransactionMessage } from '@solana/web3.js';
import { createTransferCheckedInstruction, getAssociatedTokenAddress, createAssociatedTokenAccountIdempotentInstruction, TOKEN_PROGRAM_ID, createCloseAccountInstruction, getAssociatedTokenAddressSync } from '@solana/spl-token';
import SuccessResponse from '../../interfaces/SuccessResponse';
import logger from '../../components/winston';
import { USDC_ADDRESS, WSOL_ADDRESS, WSOL_DECIMALS, USDC_DECIMALS, USDC_PROGRAM_ID, BLOXROUTE_TIP_MICROLAMPORTS, BLOXROUTE_TIP_ADDRESS_PUBLICKY } from '../../components/constant';
import { isValidSolanaAddress, versionedTransactionToBase64 } from '../../components/solanaUtil';
import { getSolanaAmount, getSplTokenAmount, getCachedAccountInfo, getCachedAccountOwner } from '../../components/accountService';
import { getTokenInfo } from '../../components/ZaiService';
import { getJupiterQuote, getJupiterSwapObj } from '../../components/jupiterSwapService';
import { getRaydiumQuote, getRaydiumSwapObj } from '../../components/raydiumSwapService';
import { createTraderAPIMemoInstruction } from '@bloxroute/solana-trader-client-ts';

const router = express.Router();
const connection = new Connection(process.env.SOLANA_QUICKNODE_ENDPOINT!);

router.post<{}, SuccessResponse>('/', async (req, res) => {
  let { inputMintAddress, inputMintDecimals, price, outputMintAddress, ownerAddress, amount, slippageBps, isMax, tgUserId } = req.body;
  
  try {
    if (!isValidSolanaAddress(ownerAddress)) {
      return res.json({ code: 501, message: 'invalid address', result: {} });
    }

    let isBuy = false;
    if (inputMintAddress === WSOL_ADDRESS.toBase58()) {
      isBuy = true;
    }

    let inputMintProgramId = TOKEN_PROGRAM_ID;
    let outMintProgramId = TOKEN_PROGRAM_ID;
    const inputMintPublicKey = new PublicKey(inputMintAddress);
    const outMintPublicKey = new PublicKey(outputMintAddress);
    const ownerPublicKey = new PublicKey(ownerAddress);

    const [inputMintOwner, outMintOwner, tokenInfoMap] = await Promise.all([
      getCachedAccountOwner(connection, inputMintPublicKey), 
      getCachedAccountOwner(connection, outMintPublicKey),
      getTokenInfo(inputMintAddress, outputMintAddress, req.header('token'), tgUserId),
    ]);

    if (inputMintOwner != null) {
      inputMintProgramId = inputMintOwner;
    }
    if (outMintOwner != null) {
      outMintProgramId = outMintOwner;
    }
    
    const inputMintAccount = getAssociatedTokenAddressSync(
      inputMintPublicKey,
      ownerPublicKey,
      false,
      inputMintProgramId,
    );
    const outMintAccount = getAssociatedTokenAddressSync(
      outMintPublicKey,
      ownerPublicKey,
      false,
      outMintProgramId,
    );

    const wSolAccount = getAssociatedTokenAddressSync(
      WSOL_ADDRESS,
      ownerPublicKey,
      false,
      TOKEN_PROGRAM_ID,
    );
    const wSolAccountOwner = await getCachedAccountOwner(connection, wSolAccount);
    let createWSolAccountInstruction = null;
    if (wSolAccountOwner === null) {
      createWSolAccountInstruction = createAssociatedTokenAccountIdempotentInstruction(
        ownerPublicKey,
        wSolAccount,
        ownerPublicKey,
        WSOL_ADDRESS,
        TOKEN_PROGRAM_ID,
      );
    }

    const tokenAccountOwner = await getCachedAccountOwner(connection, outMintAccount);

    let createOutMintAccountInstruction = null;
    if (tokenAccountOwner === null) {
      createOutMintAccountInstruction = createAssociatedTokenAccountIdempotentInstruction(
        ownerPublicKey,
        outMintAccount,
        ownerPublicKey,
        outMintPublicKey,
        outMintProgramId,
      );
    }

    let closeTokenTransferInstruction = null;
    if (!isBuy && isMax) {
      closeTokenTransferInstruction = createCloseAccountInstruction(inputMintAccount, ownerPublicKey, ownerPublicKey, [], inputMintProgramId);
    }

    const raydiumQuotePromise = getRaydiumQuote(inputMintAddress, outputMintAddress, amount, slippageBps, tgUserId, tokenInfoMap);
    const jupiterQuotePromise = getJupiterQuote(inputMintAddress, outputMintAddress, amount, slippageBps, tgUserId, tokenInfoMap);
    let chainAmountPromise = null;
    if (isBuy) {
      chainAmountPromise = getSolanaAmount(connection, ownerPublicKey, tgUserId);
    } else {
      chainAmountPromise = getSplTokenAmount(connection, inputMintAccount, tgUserId);
    }
    const [raydiumQuote, jupiterQuote, chainAmount] = await Promise.all([raydiumQuotePromise, jupiterQuotePromise, chainAmountPromise]);
    
    if (chainAmount === null) {
      return res.json({ code: 508, message: 'insufficient funds', result: {} });
    } else {
      if (amount > chainAmount) {
        return res.json({ code: 502, message: 'insufficient funds', result: {} });
      }
    }

    const priorityFee = 1000000; // await getCurrentPriorityFee(3)
    const computeUnitLimit = 1000000;
    let swapTransactionBuf: Buffer;
    const { blockhash } = await connection.getLatestBlockhash();
    
    // jupiter and raydium error
    if ((jupiterQuote.error === 0 || jupiterQuote.error === -2) && (raydiumQuote.error === 0 || raydiumQuote.error === -2)) {
      const [jupiterResult, raydiumResult] = await Promise.all([
        getJupiterSwapObj(ownerAddress, jupiterQuote.quote!, tgUserId),
        getRaydiumSwapObj(inputMintAddress, inputMintAccount, outputMintAddress, outMintAccount, ownerAddress, raydiumQuote.quote!, tgUserId),
      ]);
      if ((raydiumResult.error === 0 && raydiumQuote.error === 0) && (jupiterResult.error === 0 && jupiterQuote.error === 0)) {
        if (jupiterResult.swapTransactionBuf!.length <= raydiumResult.swapTransactionBuf!.length) {
          swapTransactionBuf = jupiterResult.swapTransactionBuf!;
        } else {
          swapTransactionBuf = raydiumResult.swapTransactionBuf!;
        }
      } else if (raydiumResult.error === 0 && raydiumQuote.error === 0) {
        swapTransactionBuf = raydiumResult.swapTransactionBuf!;
      } else if (jupiterResult.error === 0 && jupiterQuote.error === 0) {
        swapTransactionBuf = jupiterResult.swapTransactionBuf!;
      } else if (raydiumResult.error === 0 && raydiumQuote.error === -2) {
        return res.json({ code: 510, message: raydiumQuote.message!, result: {} });
      } else if (jupiterResult.error === 0 && jupiterQuote.error === -2) {
        return res.json({ code: 510, message: jupiterQuote.message!, result: {} });
      } else {
        return res.json({ code: 509, message: 'swap failed', result: {} });
      }
    } else if (raydiumQuote.error === 0 || raydiumQuote.error === -2) {
      const raydiumSwapObj = await getRaydiumSwapObj(inputMintAddress, inputMintAccount, outputMintAddress, outMintAccount, ownerAddress, raydiumQuote.quote!, tgUserId);
      if (raydiumSwapObj.error === 0) {
        if (raydiumQuote.error === 0) {
          swapTransactionBuf = raydiumSwapObj.swapTransactionBuf!;
        } else {
          return res.json({ code: 510, message: raydiumQuote.message!, result: {} });
        }
      } else {
        return res.json({ code: 509, message: 'raydium swap failed', result: {} });
      }
    } else if (jupiterQuote.error === 0 || jupiterQuote.error === -2) {
      const jupiterSwapObj = await getJupiterSwapObj(ownerAddress, jupiterQuote.quote!, tgUserId);
      if (jupiterSwapObj.error === 0) {
        if (jupiterQuote.error === 0) {
          swapTransactionBuf = jupiterSwapObj.swapTransactionBuf!;
        } else {
          return res.json({ code: 510, message: jupiterQuote.message!, result: {} });
        }
      } else {
        return res.json({ code: 509, message: 'jupiter swap failed', result: {} });
      }
    } else {
      return res.json({ code: 511, message: 'swap quote failed', result: {} });
    }
    
    var transaction = VersionedTransaction.deserialize(swapTransactionBuf as Buffer);
    const altAddresses = [
      ...transaction.message.addressTableLookups.map((lookup) => lookup.accountKey),
      new PublicKey(process.env.CUSTOM_ALT_ADDRESS!),
    ];
    const addressLookupTableAccounts = await Promise.all(
      altAddresses.map(async (altAddress) => {
        const data = await getCachedAccountInfo(connection, altAddress);
        return new AddressLookupTableAccount({
          key: altAddress,
          state: AddressLookupTableAccount.deserialize(data),
        });
      }),
    );

    var message = TransactionMessage.decompile(transaction.message, { addressLookupTableAccounts: addressLookupTableAccounts });

    if (createWSolAccountInstruction !== null) {
      message.instructions.unshift(createWSolAccountInstruction);
    }

    if (createOutMintAccountInstruction !== null) {
      message.instructions.unshift(createOutMintAccountInstruction);
    }

    const computeUnitLimitIxIndex = message.instructions.findIndex((ix) => ix.programId.equals(ComputeBudgetProgram.programId) && ix.data[0] === ComputeBudgetProgram.setComputeUnitLimit({ units: 0 }).data[0]);
    if (computeUnitLimitIxIndex !== -1) {
      const newComputeUnitLimitIx = ComputeBudgetProgram.setComputeUnitLimit({
        units: computeUnitLimit,
      });
      message.instructions[computeUnitLimitIxIndex] = newComputeUnitLimitIx;
    }

    const priorityFeeIxIndex = message.instructions.findIndex((ix) => ix.programId.equals(ComputeBudgetProgram.programId) && ix.data[0] === ComputeBudgetProgram.setComputeUnitPrice({ microLamports: 0 }).data[0]);
    if (priorityFeeIxIndex !== -1) {
      const newPriorityFeeIx = ComputeBudgetProgram.setComputeUnitPrice({
        microLamports: priorityFee,
      });
      message.instructions[priorityFeeIxIndex] = newPriorityFeeIx;
    }

    if (closeTokenTransferInstruction !== null) {
      message.instructions.push(closeTokenTransferInstruction);
    }

    if (swapTransactionBuf.length < 900) {
      const bloxrouteMemoInstruction = createTraderAPIMemoInstruction('Powered by bloXroute Trader Api');
      message.instructions.push(bloxrouteMemoInstruction);

      const bloxrouteTipInstruaction = SystemProgram.transfer({
        fromPubkey: ownerPublicKey,
        toPubkey: BLOXROUTE_TIP_ADDRESS_PUBLICKY,
        lamports: BLOXROUTE_TIP_MICROLAMPORTS,
      });
      message.instructions.push(bloxrouteTipInstruaction);
    }

    message.payerKey = ownerPublicKey;
    transaction.message = message.compileToV0Message(addressLookupTableAccounts);
    
    transaction.message.recentBlockhash = blockhash;

    transaction.signatures = Array(transaction.message.header.numRequiredSignatures).fill(new Uint8Array(64));

    return res.json({ code: 200, message: 'success', result: { transaction: versionedTransactionToBase64(transaction) } });
  } catch (error) {
    if (error instanceof Error) {
      logger.error(`Error transaction swap ${error.message}`, { tgUserId });
      return res.json({ code: 500, message: error.message, result: {} });
    } else {
      logger.error('Error transaction swap2:', String(error), { tgUserId });
      return res.json({ code: 500, message: String(error), result: {} });
    }
  }
});

export default router;
