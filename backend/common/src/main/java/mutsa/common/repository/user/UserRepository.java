package mutsa.common.repository.user;

import mutsa.common.domain.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
