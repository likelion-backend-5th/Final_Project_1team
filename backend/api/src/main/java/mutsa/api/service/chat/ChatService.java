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

    /**
     * @param chatRequestDto
     * @return 들어온 채팅을 관리합니다.
     */
    @Transactional
    public void sendMessage(ChatRequestDto chatRequestDto, String currentUsername) {
        Gson gson = new Gson();
        User user = userModuleService.getByUsername(currentUsername);
        Chatroom chatroom = chatroomService.getByApiId(chatRequestDto.getRoomApiId());
        //채팅 저장
        ChatRedis chatRedis = ChatRedis.of(chatroom, user, chatRequestDto.getMessage());
        chatRedisRepository.saveMessage(chatRedis); //채팅 저장

        //반환 정보
        ChatResponseDto chatResponseDto = ChatResponseDto.fromEntity(chatRedis, chatroom.getApiId());
        String json = gson.toJson(chatResponseDto);

        redisTemplate.convertAndSend(channelTopic.getTopic(), json);
    }

    /**
     * @param roomApiId
     * @return 이전 메세지를 불러오는 기능(레디스 정보를 파싱해서 가져옵니다)
     */
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
            responseDtos.add(new ChatResponseDto("ADMIN", "", "빈방임", roomApiId));
        }

        return responseDtos;
    }
}
