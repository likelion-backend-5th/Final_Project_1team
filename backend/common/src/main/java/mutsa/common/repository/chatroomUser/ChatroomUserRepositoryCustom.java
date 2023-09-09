package mutsa.common.repository.chatroomUser;

import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.chatroom.ChatroomUserResult;

import java.util.List;
import java.util.Optional;

public interface ChatroomUserRepositoryCustom {
    Optional<Chatroom> findByChatroomWithUsers(User user1, User user2);

    List<ChatroomUserResult> findByUser(User user);
}