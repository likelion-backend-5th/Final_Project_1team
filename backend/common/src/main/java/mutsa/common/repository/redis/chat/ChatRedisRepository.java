package mutsa.common.repository.redis.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.adapter.LocalDateTimeAdapter;
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
    public static final String ROOM_KEY = "room:%s";

    /**
     *
     * @param roomId
     * @param offset
     * @param size
     * @return 입장시에 이전 메세지를 출력해주는 메서드
     */
    public Set<String> getMessages(String roomId, int offset, int size) {
        String roomNameKey = String.format(ROOM_KEY, roomId);
        Set<String> messages = redisTemplate.opsForZSet().reverseRange(roomNameKey, offset, offset + size);
        log.info(String.format("Received messages by roomId:%s, offset:%s, size:%s ", roomId, offset, size));
        return messages;
    }

    public void saveMessage(ChatRedis message) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()) //time저장시에 오류로 추가하였는데 삭제 가능여부 확인 필요
                .create();

        String roomKey = String.format(ROOM_KEY, message.getChatroomId());
        String saveMessage = gson.toJson(message);//(레디스에 toString으로 저장한다)
        //시간을 더블형으로 변환하여 저장(score 에 해당하여 해당 컬럼 기준으로 정렬하여 조회할 수 있음)
        redisTemplate.opsForZSet().add(roomKey, saveMessage, getTimeToDouble(message.getCreatedAt()));
    }

    public double getTimeToDouble(LocalDateTime createdAt) {
        long milliseconds = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // LocalDateTime을 밀리초 단위로 변환
        return (double) milliseconds / 1000.0; // 밀리초를 초 단위로 변환
    }
}
