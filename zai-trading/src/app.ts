import express from 'express';
import morgan from 'morgan';
import helmet from 'helmet';
import cors from 'cors';
import fs from 'fs';
import axios from 'axios';
import timeout from 'connect-timeout';
import '../src/components/dotenvConfig';
import '../src/components/redis';

import * as middlewares from './middlewares';
import api from './api';

const app = express();

axios.defaults.proxy = false;

const accessLogStream = fs.createWriteStream('logs/request.log', { flags: 'a' });

app.use(morgan('combined', { stream: accessLogStream }));
app.use(helmet());
app.use(cors());
app.use(express.json());
app.use(timeout('90s'));

app.use('/zai-trading', api);

app.use(middlewares.notFound);
app.use(middlewares.errorHandler);

export default app;
