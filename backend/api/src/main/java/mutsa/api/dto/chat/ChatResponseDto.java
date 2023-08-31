package mutsa.api.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.common.domain.models.chat.ChatRedis;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class ChatResponseDto {
    private String from;
    private String date;
    private String message;
    private String chatroomApiId;

    public static ChatResponseDto fromEntity(ChatRedis chat, String username, String chatroomApiId) {
        ChatResponseDto chatResponseDto = new ChatResponseDto();
        chatResponseDto.date = chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        chatResponseDto.from = username;
        chatResponseDto.message = chat.getContent();
        chatResponseDto.chatroomApiId = chatroomApiId;
        return chatResponseDto;
    }
}
