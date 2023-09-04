import React, {useContext, useEffect, useState} from 'react';

import ChatRoomListForm from './ChatRoomListForm';
import { Chatroom } from '../../types/chat';
import { getChatroomHandler } from '../../store/auth-action';



const ChatRoomList: React.FC = () => {
    const [chatRooms, setChatRooms] = useState<Chatroom[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        setLoading(true);
        getChatroomHandler().then((response: { data: Chatroom[] } | null) => {
            if (response != null) {
                console.log('채팅방 얻어옴');
                setChatRooms(response.data)
            }
        })
        setLoading(false);
    }, []);

    return <ChatRoomListForm chatRooms={chatRooms}/>;
};

export default ChatRoomList;