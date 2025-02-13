import * as Redis from 'ioredis';
import Redlock, { Lock } from 'redlock';
import logger from './winston';

class RedisCache {
  private redis: Redis.Cluster;

  private redlock: Redlock;

  constructor() {
    this.redis = new Redis.Cluster([
      {
        host: process.env.REDIS_HOST!, 
        port: Number.parseInt(process.env.REDIS_PORT!),
      },
    ], {
      redisOptions: {
        reconnectOnError: (err) => {
          logger.error('Redis reconnect on error:', err);
          return true;
        },
      },
    });

    this.redis.on('error', (error) => {
      logger.error('Redis connection error:', error);
    });

    this.redlock = new Redlock([this.redis], {
      retryCount: 5, 
      retryDelay: 1000, 
      retryJitter: 200,
    });

    this.redlock.on('clientError', (err) => {
      console.error('Redis client error:', err);
    });
  }

 
  async setLock(lockKey: string, time: number): Promise<Lock | null> {
    try {
      const lock = await this.redlock.acquire([lockKey], time * 1000);
      return lock;
    } catch (err) {
      logger.warn(`Failed to acquire lock: ${lockKey}`, err);
      return null;
    }
  } 

  
  async unLock(lock: Lock): Promise<boolean> {
    try {
      await lock.release();
      return true;
    } catch (err) {
      logger.error('Failed to release lock:', err);
      return false;
    }
  }

  async getString(key: string): Promise<string | null> {
    try {
      const cachedData = await this.redis.get(key);
      return cachedData || null;
    } catch (error) {
      logger.error('Error redis getString:', error);
      return null;
    }
  }

  async setString(key: string, value: string, ttl: number = 3600): Promise<void> {
    try {
      await this.redis.set(key, value, 'EX', ttl);
    } catch (error) {
      logger.error(`Error redis setString key=${key}:`, error);
    }
  }

  async getObject<T>(key: string): Promise<T | null> {
    try {
      const cachedData = await this.redis.get(key);
      return cachedData ? JSON.parse(cachedData) as T : null;
    } catch (error) {
      logger.error(`Error redis getObject key=${key}:`, error);
      return null;
    }
  }

  async setObject(key: string, value: any, ttl: number = 3600): Promise<void> {
    try {
      await this.redis.set(key, JSON.stringify(value), 'EX', ttl);
    } catch (error) {
      logger.error(`Error redis setObject key=${key}:`, error);
    }
  }

  async deleteKey(key: string): Promise<boolean> {
    try {
      const result = await this.redis.del(key);
      return result > 0;
    } catch (error) {
      logger.error('Error redis deleteKey:', error);
      return false;
    }
  }

  async disconnect() {
    try {
      await this.redis.quit();
      logger.info('Redis connection closed');
    } catch (error) {
      logger.error('Error closing Redis connection:', error);
    }
  }
}

const redisCache = new RedisCache();

export { redisCache };
