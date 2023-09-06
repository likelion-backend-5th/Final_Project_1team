export type Chatroom = {
  chatroomApiId: number;
  roomName: string;
};

export type ChatroomDetail = {
  chatroomApiId: number;
  roomName: string;
  articleTitle: string;
  articleDescription: string;
  articleUsername: string;
};

export type ChatMessage = {
  from: string;
  date: string;
  message: string;
  chatroomApiId: string;
};

export type PubSubMessage<T> = {
  type: string;
  data: T;
};
