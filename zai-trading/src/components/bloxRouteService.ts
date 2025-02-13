import logger from './winston';
import WebSocket from 'ws';

class BloxRouteService {
  private ws: WebSocket | null = null;
  
  private heartbeatInterval: NodeJS.Timeout | null = null;

  private readonly HEARTBEAT_INTERVAL = 30000; 

  constructor() {
    this.initializeWebSocket();
  }

  private stopHeartbeat() {
    if (this.heartbeatInterval) {
      clearInterval(this.heartbeatInterval);
    }
  }

  private startHeartbeat() {
    this.stopHeartbeat(); 
    this.heartbeatInterval = setInterval(() => {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.ping(); 
      }
    }, this.HEARTBEAT_INTERVAL);
  }

  private reconnectWebSocket() {
    logger.info('Attempting to reconnect WebSocket...');
    this.stopHeartbeat();
    setTimeout(() => this.initializeWebSocket(), 5000); 
  }

  private initializeWebSocket() {
    this.ws = new WebSocket(process.env.SOLANA_BLOXROUTE_WSS_ENDPOINT!, {
      headers: {
        Authorization: process.env.BLOXROUTE_AUTHORIZATION_HEADER!,
      },
      rejectUnauthorized: false,
    });

    this.ws.on('open', () => {
      logger.info('WebSocket connection established.');
      this.startHeartbeat(); 
    });

    this.ws.on('message', (response) => {
      logger.info('bloxroute response', { response: response.toString() });
    });

    this.ws.on('error', (error) => {
      logger.error('WebSocket error', { error });
      this.reconnectWebSocket(); 
    });

    this.ws.on('close', (code, reason) => {
      logger.info(`WebSocket connection closed. Code: ${code}, Reason: ${reason}`);
      this.reconnectWebSocket(); 
    });
  }

  public sendTransaction(transactionBuffer: Buffer, tgUserId: string): void {
    try {
      const serializedTx = transactionBuffer.toString('base64');
      const tx = {
        jsonrpc: '2.0',
        id: 1,
        method: 'PostSubmit',
        params: {
          transaction: { content: serializedTx },
          frontRunningProtection: false,
          useStakedRPCs: true,
        },
      };
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify(tx));
      } else {
        logger.warn('WebSocket is not open, unable to send transaction.', { tgUserId });
      }
    } catch (error) {
      logger.error('bloxroute fail', error, { tgUserId });
    }
  }
}

const bloxRouteService = new BloxRouteService();

export default bloxRouteService;
