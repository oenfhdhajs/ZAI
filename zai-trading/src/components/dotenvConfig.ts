import * as dotenv from 'dotenv';

const env = process.env.ENV_PROFILE || 'release';

dotenv.config({ path: `.env.${env}` });

console.info('process.env.ENV_PROFILE:', process.env.ENV_PROFILE);
console.log(`Loaded environment: ${env}`);
