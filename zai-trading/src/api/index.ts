import express from 'express';

import outer from './outer';

const router = express.Router();

router.use('/outer', outer);

export default router;
