package mutsa.common.repository.chatroom;

import mutsa.common.domain.models.chatroom.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByApiId(String apiId);
}
