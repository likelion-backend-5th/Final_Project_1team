package mutsa.api.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mutsa.common.domain.models.chatroom.Chatroom;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetailDto {

    private String chatroomApiId;
    private String roomName;
    private String articleTitle;
    private String articleDescription;
    private String articleUsername;

    public static ChatRoomDetailDto fromEntity(
            Chatroom chatroom,
            String roomName,
            String articleTitle,
            String articleDescription,
            String articleUsername
    ) {
        ChatRoomDetailDto dto = new ChatRoomDetailDto();
        dto.chatroomApiId = chatroom.getApiId();
        dto.roomName = roomName;
        dto.articleTitle = articleTitle;
        dto.articleDescription = articleDescription;
        dto.articleUsername = articleUsername;
        return dto;
    }
}
