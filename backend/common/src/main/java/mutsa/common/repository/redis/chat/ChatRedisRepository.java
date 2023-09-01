package mutsa.common.repository.redis.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.LocalDateTimeAdapter;
import mutsa.common.domain.models.chat.ChatRedis;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ChatRedisRepository {
    private final StringRedisTemplate redisTemplate;

    private static final String USER_ROOMS_KEY = "user:%d:rooms";
    private static final String ROOM_NAME_KEY = "room:%s:name";
    public static final String ROOM_KEY = "room:%s";

    public Set<String> getMessages(String roomId, int offset, int size) {
        String roomNameKey = String.format(ROOM_KEY, roomId);
        Set<String> messages = redisTemplate.opsForZSet().reverseRange(roomNameKey, offset, offset + size);
        log.info(String.format("Received messages by roomId:%s, offset:%s, size:%s ", roomId, offset, size));
        return messages;
    }

    public void sendMessageToRedis(String topic, String serializedMessage) {
        log.info(String.format("Saving message to Redis: topic:%s, message:%s ", topic, serializedMessage));
        redisTemplate.convertAndSend(topic, serializedMessage);
    }

    public void saveMessage(ChatRedis message) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String roomKey = String.format(ROOM_KEY, message.getChatroomId());

        redisTemplate.opsForZSet().add(roomKey, gson.toJson(message), getTimeToDouble(message.getCreatedAt()));
    }

    public double getTimeToDouble(LocalDateTime createdAt) {
        long milliseconds = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // LocalDateTime을 밀리초 단위로 변환
        return (double) milliseconds / 1000.0; // 밀리초를 초 단위로 변환
    }
}
