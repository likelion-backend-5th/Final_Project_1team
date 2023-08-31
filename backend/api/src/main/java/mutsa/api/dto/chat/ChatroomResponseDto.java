package mutsa.api.dto.chat;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mutsa.common.domain.models.chatroom.Chatroom;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomResponseDto {
    private String chatroomApiId;
    private String roomName;

    //채팅방 이름은 상대방의 이름으로 한다.
    public static ChatroomResponseDto fromEntity(Chatroom chatroom, String roomName) {
        ChatroomResponseDto dto = new ChatroomResponseDto();
        dto.chatroomApiId = chatroom.getApiId();
        dto.roomName = roomName;
        return dto;
    }
}
