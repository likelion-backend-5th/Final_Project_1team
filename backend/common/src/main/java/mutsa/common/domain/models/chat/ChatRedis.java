package mutsa.common.domain.models.chat;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.domain.models.user.User;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = "chat")
public class ChatRedis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private final String apiId = UUID.randomUUID().toString();
    private String content;
    private Long userId; //유저 이름을 저장
    private Long chatroomId;
    private LocalDateTime createdAt;

    public static ChatRedis of(Chatroom chatroom, User user, String message) {
        ChatRedis chat = ChatRedis.builder()
                .id(UUID.randomUUID().toString())
                .content(message)
                .chatroomId(chatroom.getId())
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .build();
        return chat;
    }

}
