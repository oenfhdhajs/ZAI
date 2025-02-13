import express from 'express';

import walletSwap from './wallet/walletSwap';
import walletTranfer from './wallet/walletTransfer';
import walletProxy from './wallet/walletProxy';

const router = express.Router();

router.get<{}, String>('/health-check', (req, res) => {
  res.json(new Date().toString());
});

router.use('/wallet/transfer', walletTranfer);

router.use('/wallet/swap', walletSwap);

router.use('/wallet/proxy', walletProxy);

export default router;
