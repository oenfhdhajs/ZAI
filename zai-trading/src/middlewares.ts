import { NextFunction, Request, Response } from 'express';

import ErrorResponse from './interfaces/ErrorResponse';
import logger from './components/winston';

/**
 * 404
 */
export function notFound(req: Request, res: Response, next: NextFunction) {
  res.status(404);
  const error = new Error(`🔍 - Not Found - ${req.originalUrl}`);
  next(error);
}

/**
 * global
 */
// eslint-disable-next-line @typescript-eslint/no-unused-vars
export function errorHandler(err: Error, req: Request, res: Response<ErrorResponse>, next: NextFunction) {
  const statusCode = res.statusCode !== 200 ? res.statusCode : 500;
  res.status(statusCode);

  logger.error(`🔍 - ERROR:
    Method: ${req.method}
    URL: ${req.originalUrl}
    Status: ${statusCode}
    Error Stack: ${err.stack || 'No stack available'}
    Message: ${err.message}`);

  if (err.message === 'Response timeout') {
    return res.json({
      code: 504,
      result: {},
      message: 'request timeout',
    });
  }

  return res.json({
    code: statusCode,
    result: {},
    message: err.message || 'Internal Server Error',
  });
}
