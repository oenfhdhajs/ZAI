import { http } from "@/api/core";

interface Trans {
  transaction: string;
}

interface TransactionStatus {
  account: string;
  confirmationStatus: string;
}
export function sendTrans(data: any): Promise<Trans> {
  return http.requestObject({
    url: "/web/transaction/send",
    method: "post",
    data,
  }); 
}

export function checkTransStatus(data: any): Promise<TransactionStatus> {
    return http.requestObject({
      url: "/web/transaction/status",
      method: "post",
      data,
    }); 
  }

  export function pushTransStatus(data: any): Promise<TransactionStatus> {
    return http.requestObject({
      url: "/web/webBot/msg/status",
      method: "post",
      data,
    }); 
  }
  