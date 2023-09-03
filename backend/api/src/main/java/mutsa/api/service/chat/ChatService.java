package mutsa.api.service.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final UserModuleService userModuleService;
    private final ChatroomService chatroomService;
    private final ChatRedisRepository chatRedisRepository;
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public void sendMessage(ChatRequestDto chatRequestDto) {
        Gson gson = new Gson();
        User user = userModuleService.getByApiId(chatRequestDto.getUserApiId());
        Chatroom chatroom = chatroomService.getByApiId(chatRequestDto.getRoomApiId());
        String json = "";
        //채팅 저장
        ChatRedis chatRedis = ChatRedis.of(chatroom, user, chatRequestDto.getMessage());
        chatRedisRepository.saveMessage(chatRedis);

        //반환 정보
        ChatResponseDto chatResponseDto = ChatResponseDto.fromEntity(chatRedis, chatroom.getApiId());
        json = gson.toJson(chatResponseDto);
        log.info("채팅 정보 : {}", json);

        redisTemplate.convertAndSend(channelTopic.getTopic(), json);
    }

    public List<ChatResponseDto> getLastMessages(String roomApiId) {

        List<ChatResponseDto> responseDtos = new ArrayList<>();
        if (chatroomService.getByApiId(roomApiId) != null) {

            int offset = 0;
            int size = 200;
            Set<String> values = chatRedisRepository.getMessages(roomApiId, offset, size);
            for (String value : values) {
                ChatResponseDto chatResponseDto = null;
                try {
                    ChatRedis chat = objectMapper.readValue(value, ChatRedis.class);
                    chatResponseDto = ChatResponseDto.fromEntity(chat, roomApiId);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                responseDtos.add(chatResponseDto);
            }
        } else {
            responseDtos.add(new ChatResponseDto("", "", "빈방임", roomApiId));
        }

        return responseDtos;
    }
}
