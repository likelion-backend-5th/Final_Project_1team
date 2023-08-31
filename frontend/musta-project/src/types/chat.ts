export type Chatroom = {
    chatroomApiId: number,
    roomName: string
}

export type ChatroomDetail = {
    chatroomApiId: number,
    roomName: string
}

export type ChatMessage = {
    from : string,
    date : string,
    message : string,
    chatroomApiId : string
}