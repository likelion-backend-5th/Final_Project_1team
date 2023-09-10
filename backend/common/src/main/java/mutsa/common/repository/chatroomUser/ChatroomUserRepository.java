package mutsa.common.repository.chatroomUser;


import mutsa.common.domain.models.chatroomUser.ChatroomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomUserRepository extends ChatroomUserRepositoryCustom, JpaRepository<ChatroomUser, Long> {
}