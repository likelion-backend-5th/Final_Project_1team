package mutsa.common.repository.redis.chat;

import mutsa.common.domain.models.chat.ChatRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatRedisRepository extends CrudRepository<ChatRedis, String> {
    Optional<ChatRedis> findByApiId(String apiId);
}
