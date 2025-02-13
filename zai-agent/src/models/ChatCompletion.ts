// Ref: https://platform.openai.com/docs/api-reference/chat/create
export enum Role {
  System = 'system',
  User = 'user',
  Assistant = 'assistant',
}

export interface ChatMessage {
  id?: number;
  role: Role;
  messageType: MessageType;
  content: string;
  showContent: string;
  name?: string;
}

export interface ChatCompletionChoice {
  message: ChatMessage;
  finish_reason: string;
  index: number;
}

export function getRole(roleString: string): Role {
  return Role[roleString as keyof typeof Role];
}

export enum MessageType {
  Normal = 'normal',
  Error = 'error',
}

export function getMessageType(messageTypeString: string): MessageType {
  return MessageType[messageTypeString as keyof typeof MessageType];
}

export interface ChatChunk {
  oneQuestId: string;
  action: string;
  text?: string;
}

export const createTextChunk = (msg: string) => {
  return {
    action: "text",
    text: msg,
  }

}