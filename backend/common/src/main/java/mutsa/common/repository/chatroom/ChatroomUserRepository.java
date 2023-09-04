package mutsa.common.repository.chatroom;

import mutsa.common.domain.models.chatroom.Chatroom;
import mutsa.common.domain.models.chatroom.ChatroomUser;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.chatroom.ChatroomUserResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatroomUserRepository extends JpaRepository<ChatroomUser, Long> {
    @Query("SELECT cu.chatroom " +
            "FROM ChatroomUser cu " +
            "WHERE cu.user IN (:user1, :user2)" +
            "GROUP BY cu.chatroom " +
            "HAVING COUNT(DISTINCT cu.user) = 2")
    Optional<Chatroom> findByChatroomWithUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT new mutsa.common.dto.chatroom.ChatroomUserResult(cu2.user.username,cu2.chatroom.apiId)" +
            "FROM ChatroomUser cu1 " +
            "JOIN ChatroomUser cu2 ON cu1.chatroom = cu2.chatroom " +
            "WHERE cu1.user = :user AND cu2.user != :user")
    List<ChatroomUserResult> findByUser(@Param("user") User user);
}