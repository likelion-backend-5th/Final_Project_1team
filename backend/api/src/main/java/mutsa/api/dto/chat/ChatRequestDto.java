package mutsa.api.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRequestDto {
    private String userApiId;
    private String message;
    private String roomApiId;
    private MessageType type;

    public ChatRequestDto(String userApiId, String message, String roomApiId, String type) {
        this.userApiId = userApiId;
        this.message = message;
        this.roomApiId = roomApiId;
        this.type = MessageType.valueOf(type);
    }
}
