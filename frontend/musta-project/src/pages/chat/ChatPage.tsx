import React, {useEffect, useRef, useState} from "react";
import {Params, useNavigate, useParams} from "react-router-dom";
import * as Stomp from "@stomp/stompjs";
import './ChatPage.css'
import { ChatMessage, ChatroomDetail } from "../../types/chat";
import { getEachChatroomHandler } from "../../store/auth-action";

const ChatPage: React.FC= () => {
    const client = useRef<Stomp.Client | null>(null);
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const [chatRoom, setChatRooms] = useState<ChatroomDetail>();
    const {roomId} = useParams<Params>();

    const [message, setMessage] = useState<string>("");
    const messageContainerRef = useRef<HTMLDivElement>(null);
    const [enter, setEnter] = useState<boolean>(false);
    const headers = {Authorization: "Bearer " + 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBcnRpY2xlQ29udHJvbGxlclRlc3RVc2VyMSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hcGkvYXV0aC9sb2dpbiIsImF1dGhvcml0aWVzIjpbXX0.fkAwNZ-vvk99ZnsZI-C9pdgrQ3qMjLr1bqLjG8X7sg0'};
    const navigate = useNavigate(); // useNavigate hook 사용

    const userApiId = '3277eb42-f55a-4edb-a66e-61fd94454e48';
    function findRoomDetail() {
        getEachChatroomHandler(roomId).then((response: { data: ChatroomDetail } | null) => {
            if (response != null) {
                setChatRooms(response.data)
                console.log(response.data)
            }
        });
    }

    useEffect(() => {
        console.log("testing" + roomId + " ");
        findRoomDetail(); //먼저 방의 자세한 정보를 받아온다. 
        connect(); //접속한다.
    }, []);

    const connect = async () => {
        client.current = new Stomp.Client({
            brokerURL: "ws://localhost:8080/ws",
            connectHeaders: headers,
            onConnect: async () => {
                enterRoom();
                subscribe();
                console.log("success");
            },
        });
        // 접속한다.
        client.current?.activate();
    };

    // const destination = "/user/ArticleControllerTestUser1/sub/chat/enter/" + roomId;
    const subscribe = async () => {
        // console.log(destination);
        // client.current?.subscribe(destination, (body) => {
        //     const chatlist = JSON.parse(body.body);
        //     console.log("과거기록 들어옴");

        //     setMessages(
        //         chatlist.map(
        //             (chat: { messageType: any; roomId: any; sender: any; message: any; time: any }) => ({
        //                 messageType: chat.messageType,
        //                 roomId: chat.roomId,
        //                 sender: chat.sender,
        //                 message: chat.message,
        //                 time: chat.time,
        //             })
        //         )
        //     );
        // });

        client.current?.subscribe("/pub/chat/" + roomId, (body) => {
            const parsed_body = JSON.parse(body.body);
            const {from,date,message,chatroomApiId} = parsed_body; // 파싱된 정보를 추출

            setMessages((_chat_list) => [
                ..._chat_list, //이전에 있던 데이터들
                {
                    from : from,
                    date : date,
                    message : message,
                    chatroomApiId : chatroomApiId
                }
            ]);
            findRoomDetail();
        });
    };

    const disconnect = () => {
        console.log("subDisconnect");
        outRoom();
        client.current?.deactivate();
    };

    const handleMessageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setMessage(event.target.value);
    };

    const enterRoom = () => {
        console.log("ENTER")
        console.log("??" + roomId+ 'ArticleControllerTestUser1')
        if (!enter) {
            //처음 접속 한 경우에만
            client.current?.publish({
                destination: "/pub/chat/message",
                body: JSON.stringify({
                    userApiId : userApiId,
                    message : '안녕',
                    roomApiId :  roomId
                }),
            });
            console.log("여기까지 오케이")
            setEnter(true);
        }
        console.log("접속 완료")
    };

    const outRoom = () => {
        setEnter(false);
    };

    const handleSendMessage = () => {
        client.current?.publish({
            destination: "/pub/chat/message",
            body: JSON.stringify({
                userApiId : userApiId,
                message : '안녕',
                roomApiId :  roomId
            }),
        });
        setMessage("");
    };

    const handleOutChatRoom = () => {
        disconnect();
        navigate("/chatroomlist-view");
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
                <div> 판매자 : {chatRoom?.chatroomApiId}</div>
                <div> 제품 설명 : {chatRoom?.roomName}</div>
            </div>
            <div className="chat-messages-container" ref={messageContainerRef}>
                {messages.map((msg, index) => (
                    <div key={index}
                         className={`chat-bubble ${msg.from === 'ArticleControllerTestUser1'? "mine" : "theirs"}`}>
                        <div
                            className={`chat-message-writer ${msg.from === 'ArticleControllerTestUser1'? "other" : "me"}`}>
                            {msg.from}
                        </div>
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

