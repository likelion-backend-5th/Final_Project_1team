import { useNavigate } from 'react-router-dom';

import { Chatroom } from '../../types/chat';
import {
  Avatar,
  Box,
  ListItem,
  ListItemText,
  styled,
} from '@mui/material';

type Props = {
  chatRooms: Chatroom[];
};

const StyledListItem = styled(ListItem)`
  display: flex;
  align-items: center;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  transition: background-color 0.3s;
  cursor: pointer;
  &:hover {
    background-color: rgba(0, 0, 0, 0.05);
  }
`;

const StyledBox = styled(Box)`
  display: flex;
  margin-bottom: 2%;
`;

const UserAvatar = styled(Avatar)`
  width: 48px;
  height: 48px;
  margin-right: 12px;
`;

const ChatRoomText = styled(ListItemText)`
  flex: 1;
`;

const ChatRoomListForm: React.FC<Props> = ({ chatRooms }) => {
  const navigate = useNavigate();

  const handleChatRoomClick = (chatRoom: Chatroom) => {
    navigate(`/chatroom/${chatRoom.chatroomApiId}`);
  };

  return (
    <div className="container mt-4">
      <h1>나의 채팅방 </h1>
      {chatRooms.map((chatRoom) => (
        <StyledListItem
          key={chatRoom.chatroomApiId}
          onClick={() => handleChatRoomClick(chatRoom)}
          style={{ maxWidth: '75%', margin: '0 auto' }}>
          <div style={{ width: '100%' }}>
            <StyledBox>
              <UserAvatar
                alt={chatRoom.roomName}
                src="/path/to/user-image.jpg"
              />
              <ChatRoomText primary={chatRoom.roomName} />
            </StyledBox>
          </div>
        </StyledListItem>
      ))}
    </div>
  );
};

export default ChatRoomListForm;
