package mutsa.common.repository.user;

import java.util.Optional;
import mutsa.common.domain.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findByUsername(String username);

    Optional<User> findByApiId(String apiId);

    Optional<User> findByEmail(String email);
}
