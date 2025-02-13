import { http  } from "@/api/core";

export interface User {
  tgUserId: number;
  tgUserIdStr: string;
  isPremium: boolean;
  friendTgUserId: number;
  walletAddress: string;
  totalCoins: number;
  totalUserNum: number;
  shareUrl: string;
  copyShareUrl: string;
  beginTime: number;
  endTime: number;
  name: string;
  token: string;
  progress: number;
  address: string;
}

export interface NonceInfo {
  uId: string,
}

export function login(data?: object): Promise<User> {
  return http.requestObject({
    url: "/auth/me",
    method: "post",
    data
  });
}

export function sync(): Promise<User> {
  return http.requestObject({
    url: "/clicker/sync",
    method: "get",
  });
}

export function setWalletAddress(walletAddress: string): Promise<User> {
  return http.requestObject({
    url: "/clicker/walletAddress",
    method: "post",
    data: {
      walletAddress,
    }
  });
}

export function getSignNonce(params?: object): Promise<NonceInfo> {
  return http.requestObject({
    url: "/auth/verify_number",
    method: "get",
    params
  });
}