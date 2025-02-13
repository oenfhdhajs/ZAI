import { Connection, PublicKey } from '@solana/web3.js';
import { redisCache } from './redis';
import { REDIS_ACCOUNT_INFO_KEY, REDIS_EXPIRE_DAYS_1 } from './constantRedis';

export async function getCachedAccountOwner(connection: Connection, accountPublicKey: PublicKey): Promise<PublicKey | null> {
  
  const accountInfo = await connection.getAccountInfo(accountPublicKey);
  if (accountInfo) {
    const { owner } = accountInfo;
    return owner;
  }
  return null;
}


export async function getCachedAccountInfo(connection: Connection, accountKey: PublicKey): Promise<Buffer> {
  const cacheKey = `${REDIS_ACCOUNT_INFO_KEY}:${accountKey.toBase58()}`;
    
  const cachedData = await redisCache.getString(cacheKey);
  if (cachedData) {
    return Buffer.from(cachedData, 'base64'); 
  }


  const response = await connection.getAccountInfo(accountKey);
  const data = response ? response.data : Buffer.from('', 'base64');
  await redisCache.setString(cacheKey, data.toString('base64'), REDIS_EXPIRE_DAYS_1);
  return data;
}


export async function getSplTokenAmount(connection: Connection, tokenAccount: PublicKey, tgUserId: string) {
  try {
    const tokenInfo = await connection.getTokenAccountBalance(tokenAccount);
    return tokenInfo.value.amount;
  } catch (error) {
    return null;
  }
}


export async function getSolanaAmount(connection: Connection, address: PublicKey, tgUserId: string) {
  try {
    const amount = await connection.getBalance(address);
    return amount;
  } catch (error) {
    return null;
  }
}