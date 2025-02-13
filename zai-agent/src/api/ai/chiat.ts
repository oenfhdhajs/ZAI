import { http } from "@/api/core";
import { ChatMessage } from "@/models/ChatCompletion";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function aiChat(question: string, messageId: string): Promise<any> {
  return http.chat(`/ai/chat?content=${question}&messageId=${messageId}`);
}

export function getHistoryList() {
  return http.requestList({
    url: "/ai/chat/list",
    method: "get",
  }); 
}

export function getChatLog(messageId: string): Promise<Array<ChatMessage>> {
  return http.requestList({
    url: "/ai/chat/log",
    method: "get",
    params: {
      messageId,
    }
  }); 
}

export function getChatPre() {
  return http.requestList({
    url: "/ai/chatPre",
    method: "get",
  }); 
}

export function delChat(messageId: string) {
  return http.requestList({
    url: "/ai/chat/del",
    method: "post",
    data: {
      messageId
    }
  }); 
}