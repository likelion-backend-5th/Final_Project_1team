package mutsa.api.service.chat;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.chat.ChatRequestDto;
import mutsa.api.dto.chat.ChatResponseDto;
import mutsa.api.dto.chat.MessageType;
import mutsa.api.dto.chat.PubSubMessage;
import mutsa.api.service.chatroom.ChatroomService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.models.chat.ChatRedis;
import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.redis.chat.ChatRedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public void sendMessage(ChatRequestDto chatRequestDto) {
        Gson gson = new Gson();
        User user = userModuleService.getByApiId(chatRequestDto.getUserApiId());
        Chatroom chatroom = chatroomService.getByApiId(chatRequestDto.getRoomApiId());
        String json = "";
        if (chatRequestDto.getType() == MessageType.MESSAGE) {
            //채팅 저장
            ChatRedis chatRedis = ChatRedis.of(chatroom, user, chatRequestDto.getMessage());
            chatRedisRepository.saveMessage(chatRedis);


            //반환 정보
            ChatResponseDto chatResponseDto = ChatResponseDto.fromEntity(chatRedis, chatroom.getApiId());
//        String json = gson.toJson(new PubSubMessage<>(chatRequestDto.getType(), chatResponseDto));
            json = gson.toJson(chatResponseDto);
            log.info("채팅 정보 : {}", json);


//        } else if (chatRequestDto.getType() == MessageType.USER_CONNECTED) {
//            User serializedUser = gson.fromJson(chatMessage.getData(), User.class);
//            json = gson.toJson(serializedUser);
//
//
//        }//방을 나가는 경우

            redisTemplate.convertAndSend(channelTopic.getTopic(), json);
        }
    }

}
