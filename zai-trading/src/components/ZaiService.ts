import logger from './winston';
import axios from 'axios';

export async function getTokenInfo(inputMintAddress: string, outputMintAddres: string, token: string | undefined, tgUserId: string) {
  try {
    const res = await axios.post(`${process.env.Z_SHOT_HOST!}/inner/token`, { tokenAddressList: [inputMintAddress, outputMintAddres] }, {
      headers: {
        'Content-Type': 'application/json',
        'token': token,
      },
    });
    if (res.status === 200 && res.data.code === 200) {
      return res.data.result;
    }
  } catch (error) {
    if (error instanceof Error) {
      logger.error(`error: ${error.message}`, { tgUserId, inputMintAddress, outputMintAddres });
    } else {
      logger.error(`error ${String(error)}`, { tgUserId, inputMintAddress, outputMintAddres });
    }
  }
  return {};
}