import { PublicKey } from '@solana/web3.js';
import { TOKEN_PROGRAM_ID } from '@solana/spl-token';

export const USDC_ADDRESS = new PublicKey('EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v');
export const USDC_DECIMALS = 6;
export const USDC_PRICE = 1;
export const USDC_PROGRAM_ID = TOKEN_PROGRAM_ID;

export const SOL_ADDRESS = new PublicKey('So11111111111111111111111111111111111111111');

export const WSOL_ADDRESS = new PublicKey('So11111111111111111111111111111111111111112');
export const WSOL_DECIMALS = 9;

export const MAX_PRIORITY_FEE = 2400000;
export const MIN_PRIORITY_FEE = 240000;
export const DEFAULT_PRIORITY_FEE = 800000;

export const BLOXROUTE_TIP_MICROLAMPORTS = 1000000;
export const BLOXROUTE_TIP_ADDRESS_PUBLICKY = new PublicKey('HWEoBxYs7ssKuudEjzjmpfJVX7Dvi7wescFsVx2L5yoY');

export const LIQUIDITY_SHORTAGE_MAX_GAP = 500;

export const SWAP_PRICE_IMPACT_PCT = 1;