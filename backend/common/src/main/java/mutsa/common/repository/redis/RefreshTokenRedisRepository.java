package mutsa.common.repository.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {
    private final RedisTemplate<String, String> refreshTokenTemplate;
    private final Duration REFRESH_DURATION_TTL = Duration.ofDays(2);

    public void setRefreshToken(String username, String refreshToken) {
        String key = getKey(username);
        log.info("Set refreshToken to redis {} : {}", key, refreshToken);
        refreshTokenTemplate.opsForValue().set(key, refreshToken, REFRESH_DURATION_TTL);
    }

    public Optional<String> getRefreshToken(String username) {
        String key = getKey(username);
        String refreshToken = refreshTokenTemplate.opsForValue().get(key);
        log.info("Get refreshToken from Redis {} : {}", key, refreshToken);
        return Optional.ofNullable(refreshToken);
    }

    public void removeRefreshToken(String username) {
        String key = getKey(username);
        Boolean deleted = refreshTokenTemplate.delete(key);

        if (deleted == null || !deleted) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
    }

    public String getKey(String username) {
        return "REFRESH:" + username;
    }
}
