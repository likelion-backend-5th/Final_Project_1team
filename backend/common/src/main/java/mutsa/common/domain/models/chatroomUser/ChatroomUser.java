package mutsa.common.domain.models.chatroomUser;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;
import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.domain.models.user.User;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "chatroom-user")
public class ChatroomUser extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    public static ChatroomUser of(User user, Chatroom chatroom) {
        ChatroomUser chatroomUser = new ChatroomUser();
        chatroomUser.user = user;
        chatroomUser.setChatroom(chatroom);
        return chatroomUser;
    }

    private void setChatroom(Chatroom chatroom) {
        if (!chatroom.getUsers().contains(this)) {
            chatroom.getUsers().add(this);
        }
        this.chatroom = chatroom;
    }

}
