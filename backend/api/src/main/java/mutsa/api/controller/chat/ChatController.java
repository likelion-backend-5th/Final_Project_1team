package mutsa.api.controller.chat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.chat.ChatRequestDto;
import mutsa.api.dto.chat.ChatResponseDto;
import mutsa.api.service.chat.ChatService;
import mutsa.api.service.chatroom.ChatroomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatroomService chatroomService;

    /**
     * /pub/chat/message 으로 오는 메세지를 여기서 받아서 처리한다( -> chatservice -> redisMessageSubscriber에서 모두에게 전송)
     *
     * @param chatRequestDto
     */
    @MessageMapping("/chat/message")
    public void message(
            ChatRequestDto chatRequestDto,
            SimpMessageHeaderAccessor accessor
    ) {
        log.info("채팅이 들어왔다!!!! " + chatRequestDto);
        // WebSocket 세션에서 사용자 정보 가져오기
        String username = (String)accessor.getSessionAttributes().get("username");

        chatService.sendMessage(chatRequestDto, username);
    }

    /**
     * /sub/chat/room/{roomApiId} 방에 입장하면 이전 메세지를 출력해주는 기능
     * @param roomApiId
     * @return
     */
    @SubscribeMapping("/chat/room/{roomApiId}")
    public List<ChatResponseDto> sendGreet(
            @DestinationVariable("roomApiId") String roomApiId
    ) {
        log.info("new subscription to {}", roomApiId);
        chatroomService.getByApiId(roomApiId); //방이 존재하는지 확인하는 기능(제거 가능)
        List<ChatResponseDto> messages = chatService.getLastMessages(roomApiId);
        return messages;

    }
}
