import React, { useEffect, useRef, useState } from 'react';
import { Params, useNavigate, useParams } from 'react-router-dom';
import './ChatPage.css';
import { ChatMessage, ChatroomDetail } from '../../types/chat';
import { getEachChatroomHandler } from '../../store/auth-action';
import * as Stomp from '@stomp/stompjs';
import {
  Avatar,
  Box,
  Button,
  Paper,
  ThemeProvider,
  Typography,
  createTheme,
} from '@mui/material';
import SendIcon from '@mui/icons-material/Send';

const customTheme = createTheme({
  palette: {
    secondary: {
      main: '#99FF99',
    },
  },
});

const ChatPage: React.FC = () => {
  const stompClient = useRef<Stomp.Client | null>(null);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [chatRoom, setChatRoom] = useState<ChatroomDetail | null>(null);
  const { roomId } = useParams<Params>();

  const [message, setMessage] = useState<string>('');
  const messageContainerRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const authorizationToken: string | null =
    window.localStorage.getItem('accessToken') || ''; // 또는 다른 기본값 설정

  useEffect(() => {
    // 방 상세 정보
    findRoomDetail();

    // STOMP 클라이언트 초기화및 설정
    stompClient.current = new Stomp.Client({
      brokerURL: 'ws://localhost:8080/ws',
      connectHeaders: {
        Authorization: authorizationToken,
      },
      onConnect: async () => {
        subscribe();
      },
    });

    //실행
    stompClient.current.activate();
    return () => {
      disconnect();
    };
  }, []); // 의존성 배열 비움

  const findRoomDetail = () => {
    getEachChatroomHandler(roomId).then(
      (response: { data: ChatroomDetail } | null) => {
        if (response != null) {
          setChatRoom(response.data);
        }
      }
    );
  };
  const subscribe = () => {
    if (!stompClient) {
      return;
    }

    if (!stompClient.current?.connected) {
      return;
    }

    stompClient.current?.subscribe(
      `/sub/chat/room/${roomId}`,
      (body) => {
        const parsedBodies = JSON.parse(body.body);

        if (Array.isArray(parsedBodies)) {
          // 여러개로 오는 경우
          const newMessages: any = []; // 새로운 메시지를 담을 배열

          parsedBodies.reverse().forEach((parsedBody) => {
            const { from, date, message, chatroomApiId } = parsedBody;

            // 새로운 메시지를 배열의 뒷쪽에 추가
            newMessages.push({
              from: from,
              date: date,
              message: message,
              chatroomApiId: chatroomApiId,
            });
          });

          // 이전 메시지와 새로운 메시지를 합쳐서 업데이트
          setMessages((prevMessages) => [...prevMessages, ...newMessages]);
        } else {
          const { from, date, message, chatroomApiId } = parsedBodies;

          setMessages((prevMessages) => [
            ...prevMessages,
            {
              from: from,
              date: date,
              message: message,
              chatroomApiId: chatroomApiId,
            },
          ]);
        }
      },
      {
        Authorization: authorizationToken,
      }
    );
  };

  const disconnect = () => {
    stompClient.current?.deactivate();
  };

  const handleMessageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setMessage(event.target.value);
  };

  useEffect(() => {
    if (messageContainerRef.current) {
      messageContainerRef.current.scrollTop =
        messageContainerRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSendMessage = () => {
    if (stompClient) {
      stompClient.current?.publish({
        destination: '/pub/chat/message',
        body: JSON.stringify({
          message: message,
          roomApiId: roomId,
          type: 'message',
        }),
        headers: {
          Authorization: authorizationToken,
        },
      });

      setMessage('');
    }
  };

  const handleOutChatRoom = () => {
    disconnect();
    navigate('/chatrooms');
  };

  useEffect(() => {
    if (messageContainerRef.current) {
      messageContainerRef.current.scrollTop =
        messageContainerRef.current.scrollHeight;
    }
  }, [messages]);

  const [isArticlePopupVisible, setIsArticlePopupVisible] = useState(false);

  const showArticlePopup = () => {
    setIsArticlePopupVisible(true);
  };

  const hideArticlePopup = () => {
    setIsArticlePopupVisible(false);
  };

  const handleEnterKeyPress = (
    event: React.KeyboardEvent<HTMLInputElement>
  ) => {
    if (event.key === 'Enter') {
      handleSendMessage();
    }
  };

  // 게시글 정보 보기에서 제목을 눌렀을 때 해당 게시글 페이지로 이동
  const goArticlePage = () => {
    navigate(`/article/detail/${chatRoom?.articleApiId}`);
  };

  return (
    <ThemeProvider theme={customTheme}>
      <div className="chat-page-container">
        <div className="chat-header">
          <div>{chatRoom?.roomName}님과의 채팅</div>
        </div>
        <button onClick={() => showArticlePopup()} className="btn btn-primary">
          게시글 정보 보기
        </button>
        {isArticlePopupVisible && (
          <div className="article-popup">
            <h2>게시글 정보</h2>
            <p>제목: {chatRoom?.articleTitle}</p>
            <p>내용: {chatRoom?.articleDescription}</p>
            <p>작성자: {chatRoom?.articleUsername}</p>
            <button
              onClick={goArticlePage}
              className="btn btn-primary"
              style={{ margin: '0.5rem' }}>
              해당 게시글 보기
            </button>
            <button onClick={hideArticlePopup} className="btn btn-secondary">
              닫기
            </button>
          </div>
        )}
        <div className="chat-messages-container" ref={messageContainerRef}>
          {messages.map((msg, index) =>
            msg.from === chatRoom?.roomName ? (
              <Box
                key={index}
                sx={{
                  display: 'flex',
                  justifyContent: 'flex-start',
                  mb: 2,
                }}>
                <Box
                  sx={{
                    display: 'flex',
                    flexDirection: 'row',
                    alignItems: 'center',
                  }}>
                  <Avatar sx={{ bgcolor: '#808080' }}>
                    {msg.from.slice(0, 3)}
                  </Avatar>
                  <Paper
                    variant="outlined"
                    sx={{
                      p: 2,
                      ml: 1,
                      mr: 0,
                      backgroundColor: '#808080',
                      borderRadius: '20px 20px 20px 5px',
                    }}>
                    <Typography variant="body1" style={{ color: 'white' }}>
                      {msg.message}
                    </Typography>
                    <Typography variant="caption" style={{ color: 'white' }}>
                      {msg.date}
                    </Typography>
                  </Paper>
                </Box>
              </Box>
            ) : (
              <Box
                key={index}
                sx={{
                  display: 'flex',
                  justifyContent: 'flex-end',
                  mb: 2,
                }}>
                <Box
                  sx={{
                    display: 'flex',
                    flexDirection: 'row-reverse',
                    alignItems: 'center',
                  }}>
                  <Avatar
                    sx={{ bgcolor: '#B3CFF6' }}
                    style={{ color: 'black' }}>
                    나
                  </Avatar>
                  <Paper
                    variant="outlined"
                    sx={{
                      p: 2,
                      ml: 0,
                      mr: 1,
                      backgroundColor: '#B3CFF6',
                      borderRadius: '20px 20px 5px 20px',
                    }}>
                    <Typography variant="body1">{msg.message}</Typography>
                    <Typography variant="caption" style={{ color: 'black' }}>
                      {msg.date}
                    </Typography>
                  </Paper>
                </Box>
              </Box>
            )
          )}
        </div>
        <div className="chat-input-container">
          <input
            type="text"
            placeholder="Type your message"
            value={message}
            onChange={handleMessageChange}
            onKeyPress={handleEnterKeyPress}
            style={{ color: 'black' }}
          />
          <Button onClick={handleSendMessage} startIcon={<SendIcon />}>
            전송
          </Button>
        </div>
        <button
          onClick={handleOutChatRoom}
          className="btn btn-outline-secondary">
          채팅방 나가기
        </button>
      </div>
    </ThemeProvider>
  );
};

export default ChatPage;
