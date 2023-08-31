package mutsa.api.config.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableRedisRepositories(basePackages = "mutsa.common.repository.redis" )
public class RedisConfig {
}
