import {useNavigate} from 'react-router-dom';

import { Chatroom } from '../../types/chat';


type Props = {
    chatRooms: Chatroom[];
};

const ChatRoomListForm: React.FC<Props> = ({chatRooms}) => {
    const navigate = useNavigate();
    console.log(chatRooms)

    const handleChatRoomClick = (chatRoom: Chatroom) => {
        console.log(chatRoom.chatroomApiId + '을 클릭함');
        navigate(`/chatroom/${chatRoom.chatroomApiId}`);
    };

    return (
        <div className="container mt-4">
            <h1>나의 채팅방 </h1>
            <table>
                <thead>
                <tr>
                    <th>순서</th>
                    <th>방이름</th>
                    <th>방 API ID(테스트 용)</th>
                </tr>
                </thead>
                <tbody>
                {chatRooms.map((chatRoom, index) => (
                    <tr key={chatRoom.chatroomApiId} onClick={() => handleChatRoomClick(chatRoom)}>
                        <td>{index + 1}</td>
                        <td>{chatRoom.roomName}</td>
                        <td>{chatRoom.chatroomApiId}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ChatRoomListForm;