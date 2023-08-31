package mutsa.api.service.chat;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.chat.ChatRequestDto;
import mutsa.api.dto.chat.ChatResponseDto;
import mutsa.api.service.chatroom.ChatroomService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.models.chat.ChatRedis;
import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.redis.chat.ChatRedisRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatService {
    private final UserModuleService userModuleService;
    private final ChatroomService chatroomService;
    private final ChatRedisRepository chatRedisRepository;
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    public void sendMessage(ChatRequestDto chatRequestDto) {

        User user = userModuleService.getByApiId(chatRequestDto.getUserApiId());
        Chatroom chatroom = chatroomService.getByApiId(chatRequestDto.getRoomApiId());

        ChatRedis chat = chatRedisRepository.save(ChatRedis.of(chatroom, user, chatRequestDto.getMessage()));

        String topic = channelTopic.getTopic();
        ChatResponseDto chatResponseDto = ChatResponseDto.fromEntity(chat, user.getUsername(), chatroom.getApiId());
        redisTemplate.convertAndSend(topic, chatResponseDto);

    }
}
