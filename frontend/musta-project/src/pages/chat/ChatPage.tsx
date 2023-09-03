import React, { useEffect, useRef, useState } from "react";
import { Params, useNavigate, useParams } from "react-router-dom";
import './ChatPage.css'
import { ChatMessage, ChatroomDetail } from "../../types/chat";
import { getEachChatroomHandler } from "../../store/auth-action";
import * as Stomp from "@stomp/stompjs";
const ChatPage: React.FC = () => {

    const stompClient = useRef<Stomp.Client | null>(null);
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const [chatRoom, setChatRoom] = useState<ChatroomDetail | null>(null);
    const { roomId } = useParams<Params>();

    const [message, setMessage] = useState<string>("");
    const messageContainerRef = useRef<HTMLDivElement>(null);
    const navigate = useNavigate();
    const authorizationToken: string | null = window.localStorage.getItem('token') || ''; // 또는 다른 기본값 설정

    useEffect(() => {
        // 방 상세 정보
        findRoomDetail();

        // STOMP 클라이언트 초기화및 설정
        stompClient.current = new Stomp.Client({
            brokerURL: "ws://localhost:8080/ws",
            connectHeaders: {
                Authorization: authorizationToken,
            },
            onConnect: async () => {
                subscribe();
                console.log("success");
            },
        });

        //실행
        stompClient.current.activate();
        return () => {
            disconnect();
        };

    }, []); // 의존성 배열 비움

    const findRoomDetail = () => {
        getEachChatroomHandler(roomId).then((response: { data: ChatroomDetail } | null) => {
            if (response != null) {
                setChatRoom(response.data)
                console.log(response.data)
            }
        });
    }
    const subscribe = () => {
        if (!stompClient) {
            console.log("Stomp client is not available");
            return;
        }

        if (!stompClient.current?.connected) {
            console.log("Stomp client is not connected");
            return;
        }

        console.log(`Subscribing to: /sub/chat/room/${roomId}`);
        stompClient.current?.subscribe(`/sub/chat/room/${roomId}`, (body) => {
            const parsedBodies = JSON.parse(body.body);

            if (Array.isArray(parsedBodies)) {
                // 여러개로 오는 경우
                const newMessages: any = []; // 새로운 메시지를 담을 배열

                parsedBodies.reverse().forEach((parsedBody) => {
                    const { from, date, message, chatroomApiId } = parsedBody;

                    // 새로운 메시지를 배열의 뒷쪽에 추가합니다.
                    newMessages.push({
                        from: from,
                        date: date,
                        message: message,
                        chatroomApiId: chatroomApiId,
                    });
                });

                // 이전 메시지와 새로운 메시지를 합쳐서 업데이트합니다.
                setMessages((prevMessages) => [
                    ...prevMessages,
                    ...newMessages,
                ]);
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
        }, {
            Authorization: authorizationToken,
        })


    };


    const disconnect = () => {
        console.log("Disconnecting");
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
                destination: "/pub/chat/message",
                body: JSON.stringify({
                    message: message,
                    roomApiId: roomId,
                    type: 'message',
                }),
                headers: {
                    Authorization: authorizationToken,
                },
            });
    
            setMessage("");
        }
    };
    

    const handleOutChatRoom = () => {
        disconnect();
        navigate("/chatrooms");
    };

    useEffect(() => {
        if (messageContainerRef.current) {
            messageContainerRef.current.scrollTop =
                messageContainerRef.current.scrollHeight;
        }
    }, [messages]);

    return (
        <div className="chat-page-container">
            <div className="chat-header">
                <div>채팅방 이름: {chatRoom?.roomName}</div>
            </div>
            <div className="chat-messages-container" ref={messageContainerRef}>
                {messages.map((msg, index) => (
                    <div key={index} className={`chat-bubble mine`}>
                        <div className="chat-bubble-message">{msg.message}</div>
                        <div className="chat-bubble-message">{msg.date}</div>
                    </div>
                ))}
            </div>
            <div className="chat-input-container">
                <input
                    type="text"
                    placeholder="Type your message"
                    value={message}
                    onChange={handleMessageChange}
                />
                <button onClick={handleSendMessage}>Send</button>
            </div>
            <button onClick={handleOutChatRoom} className="btn btn-outline-secondary">채팅방 나가기</button>
        </div>
    );
};

export default ChatPage;
