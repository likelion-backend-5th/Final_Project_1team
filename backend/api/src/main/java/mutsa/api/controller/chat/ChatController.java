package mutsa.api.controller.chat;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.chat.ChatRequestDto;
import mutsa.api.service.chat.ChatService;
import mutsa.api.service.chat.RedisMessageSubscriber;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * /pub/chat/message
     * @param chatRequestDto
     */
    @MessageMapping("/chat/message")
    public void message(
            ChatRequestDto chatRequestDto
    ) {
        log.info("문 열어!!!! "+chatRequestDto);
        chatService.sendMessage(chatRequestDto);
    }
}
