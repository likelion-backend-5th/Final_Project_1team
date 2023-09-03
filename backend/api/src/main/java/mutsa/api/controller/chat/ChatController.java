package mutsa.api.controller.chat;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.chat.ChatRequestDto;
import mutsa.api.dto.chat.ChatResponseDto;
import mutsa.api.service.chat.ChatService;
import mutsa.api.service.chat.RedisMessageSubscriber;
import mutsa.api.service.chatroom.ChatroomService;
import mutsa.common.domain.models.chatroom.Chatroom;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
//        List<ChatMessage> last5Messages
//                = chatService.getLast5Messages(roomApiId);

        List<ChatResponseDto> messages = new ArrayList<>();
        ChatResponseDto chatMessage = new ChatResponseDto("admin", "", "어드민 동작", roomApiId);
        messages.add(chatMessage);
//        if (last5Messages.size() > 0) {
//            int count = Math.min(last5Messages.size(), 5);
//            chatMessage.setMessage(String.format("hello! these are the last %d messages", count));
//            chatMessage.setTime(last5Messages.get(0).getTime());
//        } else {
//            chatMessage.setMessage("hello! there aren't any messages here");
//            chatMessage.setTime(new SimpleDateFormat("HH:mm").format(new Date()));
//        }
//        last5Messages.add(0, chatMessage);
        return messages;

    }
}
