package mutsa.api.controller.chat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.chat.ChatRequestDto;
import mutsa.api.dto.chat.ChatResponseDto;
import mutsa.api.service.chat.ChatService;
import mutsa.api.service.chatroom.ChatroomService;
import mutsa.common.domain.models.chatroom.Chatroom;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
     * /pub/chat/message
     *
     * @param chatRequestDto
     */
    @MessageMapping("/chat/message")
    public void message(
            ChatRequestDto chatRequestDto
    ) {
        log.info("문 열어!!!! " + chatRequestDto);
        chatService.sendMessage(chatRequestDto);
    }


    @SubscribeMapping("/chat/room/{roomApiId}")
    public List<ChatResponseDto> sendGreet(
            @DestinationVariable("roomApiId") String roomApiId
    ) {
        log.info("new subscription to {}", roomApiId);
        Chatroom chatRoom = chatroomService.getByApiId(roomApiId);
        List<ChatResponseDto> messages = chatService.getLastMessages(roomApiId);
        return messages;

    }
}
