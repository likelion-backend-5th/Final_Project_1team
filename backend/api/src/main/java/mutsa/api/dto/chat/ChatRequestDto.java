package mutsa.api.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequestDto {
    private String userApiId;
    private String message;
    private String roomApiId;

    public ChatRequestDto(String from, String message, String roomId) {
        this.userApiId = from;
        this.message = message;
        this.roomApiId = roomId;
    }
}
