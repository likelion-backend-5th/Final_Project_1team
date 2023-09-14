package mutsa.common.repository.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.domain.models.user.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserCacheRepository {
    private final RedisTemplate<String,User> userRedisTemplate;
    //레디스 공간을 효율적으로 사용하기 위해서 Dead time
    private final static Duration USER_CACHE_TTL = Duration.ofDays(2);

    public void setUser(User user) {
        String key = getKey(user.getUsername());
        log.info("Set user to redis {} : {}", key, user);
        userRedisTemplate.opsForValue().set(key, user,USER_CACHE_TTL);
    }

    public Optional<User> getUser(String username) {
        String key = getKey(username);
        User user = userRedisTemplate.opsForValue().get(key);
        log.info("Get user from Redis {} : {}", key, user);
        return Optional.ofNullable(user);
    }

    public String getKey(String username) {
        //레디스 키값 구성시 사용할 Prefix
        return "USER:" + username;
    }
}
