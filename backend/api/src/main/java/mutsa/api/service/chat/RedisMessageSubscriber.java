package mutsa.api.service.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.chat.ChatRequestDto;
import mutsa.api.dto.chat.ChatResponseDto;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            ChatResponseDto chatResponseDto = objectMapper.readValue(publishMessage, ChatResponseDto.class);
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatResponseDto.getChatroomApiId(), chatResponseDto);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.CHAT_NOT_FOUND);
        }
    }
}