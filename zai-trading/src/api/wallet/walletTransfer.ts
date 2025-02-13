import express from 'express';
import { Transaction, Connection, PublicKey, ComputeBudgetProgram, SystemProgram } from '@solana/web3.js';
import { createTransferCheckedInstruction, getAssociatedTokenAddress, createAssociatedTokenAccountIdempotentInstruction, TOKEN_PROGRAM_ID, createCloseAccountInstruction } from '@solana/spl-token';
import SuccessResponse from '../../interfaces/SuccessResponse';
import logger from '../../components/winston';
import { USDC_ADDRESS, WSOL_ADDRESS } from '../../components/constant';
import { getCachedAccountOwner } from '../../components/accountService';
import { isValidSolanaAddress } from '../../components/solanaUtil';
import { getSplTokenAmount, getSolanaAmount } from '../../components/accountService';

const router = express.Router();
const connection = new Connection(process.env.SOLANA_QUICKNODE_ENDPOINT!);

router.post<{}, SuccessResponse>('/', async (req, res) => {
  const { fromAddress, toAddress, mintAddress, amount, decimals, isMax, tgUserId } = req.body;
  try {
    if (!isValidSolanaAddress(fromAddress) || !isValidSolanaAddress(toAddress)) {
      return res.json({ code: 501, message: 'invalid address', result: {} });
    }

    if (!mintAddress) {
      return res.json({ code: 501, message: 'invalid token address', result: {} });
    }

    const fromPublicKey = new PublicKey(fromAddress);
    const toPublicKey = new PublicKey(toAddress);

    if (mintAddress === WSOL_ADDRESS.toBase58()) {
      const solAmount = await getSolanaAmount(connection, fromPublicKey, tgUserId);
      if (solAmount === null || solAmount < Number.parseInt(amount)) {
        return res.json({ code: 501, message: 'insufficient funds', result: {} });
      }
      const transaction = new Transaction().add(
        SystemProgram.transfer({
          fromPubkey: fromPublicKey,
          toPubkey: toPublicKey,
          lamports: amount,
        }),
      );
      const { blockhash } = await connection.getLatestBlockhash();
      transaction.recentBlockhash = blockhash;
      transaction.feePayer = fromPublicKey;
      const serializedTransaction = transaction.serialize({ requireAllSignatures: false });
      return res.status(200).json({ code: 200, message: 'success', result: { transaction: serializedTransaction.toString('base64') } });
    }

    let inputTokenProgramId = TOKEN_PROGRAM_ID;
    const inputAccountOwner = await getCachedAccountOwner(connection, new PublicKey(mintAddress));
    if (inputAccountOwner != null) {
      inputTokenProgramId = inputAccountOwner;
    }
    const inputTokenAccount = await getAssociatedTokenAddress(
      new PublicKey(mintAddress),
      new PublicKey(fromAddress),
      false,
      inputTokenProgramId,
    );
    const chainAmount = await getSplTokenAmount(connection, inputTokenAccount, tgUserId);
    if (chainAmount === null) {
      return res.json({ code: 508, message: 'insufficient funds', result: {} });
    } else {
      if (amount > chainAmount) {
        return res.json({ code: 401, message: 'insufficient funds', result: {} });
      }
    }
    
    const mintPublicKey = new PublicKey(mintAddress);
    let tokenProgramId = TOKEN_PROGRAM_ID;
    const mintOwner = await getCachedAccountOwner(connection, mintPublicKey);
    if (mintOwner != null) {
      tokenProgramId = mintOwner;
    }
    const fromTokenAccount = await getAssociatedTokenAddress(
      mintPublicKey,
      new PublicKey(fromAddress),
      false,
      tokenProgramId,
    );
    const toTokenAccount = await getAssociatedTokenAddress(
      mintPublicKey,
      new PublicKey(toAddress),
      false,
      tokenProgramId,
    );

    let transaction = new Transaction();
    const accountOwner = await getCachedAccountOwner(connection, toTokenAccount);
    if (accountOwner === null) {
      const createTokenTransferInstruction = createAssociatedTokenAccountIdempotentInstruction(
        fromPublicKey,
        toTokenAccount,
        new PublicKey(toAddress),
        mintPublicKey,
        tokenProgramId,
      );
      transaction.add(createTokenTransferInstruction);
    }

    const newPriorityFeeIx = ComputeBudgetProgram.setComputeUnitPrice({
      microLamports: 400000,  // await getCurrentPriorityFee(1)
    });
    transaction.add(newPriorityFeeIx);
    
    transaction.add(
      createTransferCheckedInstruction(
        fromTokenAccount,              
        mintPublicKey,                 
        toTokenAccount,                
        new PublicKey(fromAddress),    
        amount,                        
        decimals,                     
        [],                           
        tokenProgramId,                
      ),
    );
    
    if (!mintPublicKey.equals(USDC_ADDRESS) && !mintPublicKey.equals(WSOL_ADDRESS)) {
      if (isMax) {
        const closeTokenTransferInstruction = createCloseAccountInstruction(fromTokenAccount, fromPublicKey, new PublicKey(fromAddress), [], tokenProgramId);
        transaction.add(closeTokenTransferInstruction);
        logger.info('closeAccount', { tgUserId });
      }
    } 

    const { blockhash } = await connection.getLatestBlockhash();
    transaction.recentBlockhash = blockhash;
    transaction.feePayer = fromPublicKey;

    const serializedTransaction = transaction.serialize({ requireAllSignatures: false });
    return res.status(200).json({ code: 200, message: 'success', result: { transaction: serializedTransaction.toString('base64') } });
  } catch (error) {
    logger.error('Error transfer:', error);
    if (error instanceof Error) {
      res.json({ code: 500, message: error.message, result: {} });
    } else {
      res.json({ code: 500, message: String(error), result: {} });
    }
  }
});

export default router;
