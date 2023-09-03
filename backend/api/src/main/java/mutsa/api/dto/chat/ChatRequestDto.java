package mutsa.api.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRequestDto {
    private String message;
    private String roomApiId;
    private MessageType type;

    public ChatRequestDto(String message, String roomApiId, String type) {
        this.message = message;
        this.roomApiId = roomApiId;
        this.type = MessageType.valueOf(type);
    }
}
