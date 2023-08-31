export type ChatRoom = {
    chatroomApiId: number,
    roomName: string
}

export type ChatRoomDetail = {
    chatroomApiId: number,
    roomName: string
}

export type ChatMessage = {
    from : string,
    date : string,
    message : string,
    chatroomApiId : string
}