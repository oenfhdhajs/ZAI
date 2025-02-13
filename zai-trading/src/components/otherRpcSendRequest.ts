import { Connection, SendOptions } from '@solana/web3.js';
import logger from './winston';

const heliusConnection = new Connection(process.env.SOLANA_HELIUS_ENDPOINT!);
const officeConnection = new Connection(process.env.SOLANA_ENDPOINT!);
const shyftConnection = new Connection(process.env.SOLANA_SHYFT_ENDPOINT!);
const ankrConnection = new Connection(process.env.SOLANA_ANKR_ENDPOINT!);
const zanConnection = new Connection(process.env.SOLANA_ZAN_ENDPOINT!);


export const otherRpcSendRequest = async (transactionBuffer: Buffer, tgUserId: string | null) => {
  try {
    const params: SendOptions = {
      preflightCommitment: 'confirmed',
      maxRetries: 2,
      skipPreflight: true,
    };
    ankrConnection.sendRawTransaction(transactionBuffer, params);
    heliusConnection.sendRawTransaction(transactionBuffer, params);
    officeConnection.sendRawTransaction(transactionBuffer, params);
    shyftConnection.sendRawTransaction(transactionBuffer, params); 
    zanConnection.sendRawTransaction(transactionBuffer, params); 
  } catch (error) {
    logger.error('send error', error, { tgUserId });
  }
};