import express from 'express';
import { Connection, VersionedTransaction } from '@solana/web3.js';
import SuccessResponse from '../../interfaces/SuccessResponse';
import logger from '../../components/winston';
import { otherRpcSendRequest } from '../../components/otherRpcSendRequest';


const router = express.Router();
const connection = new Connection(process.env.SOLANA_QUICKNODE_ENDPOINT!);
const programLog = 'Program log: ';
const programLogSlippage = 'Slippage tolerance exceeded';
const programLogSlipCode = '0x1771';
const programLogAccountClose = 'Non-native account can only be closed if its balance is zero';
const programLogAccountClose2 = 'An account can only be closed if its withheld fee balance is zero';
const pragramVersionedTransactionToLarge = 'VersionedTransaction too large';

router.post<{}, SuccessResponse>('/', async (req, res) => {
  const { transaction, tgUserId } = req.body;
  try {
    const transactionBuffer = Buffer.from(transaction, 'base64');
    
    // We first simulate whether the transaction would be successful
    const transactionDes = VersionedTransaction.deserialize(transactionBuffer);
    const { value: simulatedTransactionResponse } = await connection.simulateTransaction(transactionDes, { replaceRecentBlockhash: true, commitment: 'processed' });

    const { err, logs } = simulatedTransactionResponse;
    if (err) {
      // Simulation error, we can check the logs for more details
      // If you are getting an invalid account error, make sure that you have the input mint account to actually swap from.
      logger.error(`simulateTransaction logs: ${logs}`, { tgUserId });
      logger.error('simulateTransaction err: ', { tgUserId, err });
      let errorMessage = 'transaction error';
      let slipPageExceeded = false;
      let accountClose = false;
      if (logs !== null && logs.length > 0) {
        for (const logString of logs) {
          const index = logString.indexOf(programLog);
          if (index !== -1) {
            errorMessage = logString.replace(programLog, '');
          }
          const slipPageIndex = logString.indexOf(programLogSlippage);
          if (slipPageIndex !== -1) {
            slipPageExceeded = true;
            break;
          }
          const slipCodeIndex = logString.indexOf(programLogSlipCode);
          if (slipCodeIndex !== -1) {
            slipPageExceeded = true;
            break;
          }
          const accountCloseIndex = logString.indexOf(programLogAccountClose);
          if (accountCloseIndex !== -1) {
            accountClose = true;
            break;
          }
          const accountClose2Index = logString.indexOf(programLogAccountClose2);
          if (accountClose2Index !== -1) {
            accountClose = true;
            break;
          }
        }
        if (slipPageExceeded) {
          logger.error('simulateTransaction transaction error slippage tolerance exceeded', { tgUserId });
          return res.json({ code: 506, message: 'slippage tolerance exceeded', result: {} });
        } else if (accountClose) {
          logger.error('simulateTransaction transaction error account close', { tgUserId });
          return res.json({ code: 507, message: 'account close', result: {} });
        } else {
          logger.error('simulateTransaction transaction error', { tgUserId, errorMessage });
          return res.json({ code: 500, message: errorMessage, result: {} });
        }
      } else {
        logger.error('simulateTransaction transaction error', { tgUserId, err });
        return res.json({ code: 500, message: err, result: {} });
      }
    }

    const txId = await connection.sendRawTransaction(transactionBuffer, {
      preflightCommitment: 'confirmed',
      maxRetries: 2,
      skipPreflight: true,
    });
    logger.info(`quicknode transaction txId: ${txId}`, { tgUserId });

    otherRpcSendRequest(transactionBuffer, tgUserId);
    
    return res.json({ code: 200, message: 'success', result: { transaction: txId } });
  } catch (error) {
    if (error instanceof Error) {
      logger.error(`Error transaction proxy: ${error.message}`, { tgUserId, size: transaction.length });
      const toLargeCodeIndex = error.message.indexOf(pragramVersionedTransactionToLarge);
      if (toLargeCodeIndex !== -1) {
        return res.json({ code: 505, message: 'transaction too large', result: {} });
      } else {
        return res.json({ code: 500, message: error.message, result: {} });
      }
    } else {
      logger.error(`Error transaction proxy2 ${String(error)}`, { tgUserId });
      res.json({ code: 500, message: String(error), result: {} });
    }
  }
});

export default router;
