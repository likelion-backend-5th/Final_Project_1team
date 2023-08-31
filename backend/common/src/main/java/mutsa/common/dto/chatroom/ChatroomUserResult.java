package mutsa.common.dto.chatroom;

import lombok.Getter;

@Getter
public class ChatroomUserResult {
    private String user;
    private String chatroom;

    public ChatroomUserResult(String user, String chatroom) {
        this.user = user;
        this.chatroom = chatroom;
    }

}
