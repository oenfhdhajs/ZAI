import bs58 from 'bs58';
import { VersionedTransaction } from '@solana/web3.js';


export const waitTime = (time: number) =>
  new Promise((resolve) => setTimeout(resolve, time));

export function isValidSolanaAddress(address: string) {
  try {
    const decoded = bs58.decode(address);
    return decoded.length === 32;
  } catch (e) {
    return false;
  }
}

export function versionedTransactionToBase64(transaction: VersionedTransaction) {
  const serializedTransaction = transaction.serialize();

  const binaryString = String.fromCharCode(...serializedTransaction);

  return btoa(binaryString);
}