package mutsa.api.config.socket;

import lombok.extern.slf4j.Slf4j;
import mutsa.api.service.chat.RedisMessageSubscriber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@Slf4j
public class RedisAppConfig {
    /**
     * 레디스 서버와의 상호작용을 위한 텤플릿
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    @Primary
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    /**
     * 실제 메세지를 처리하는 비즈니스 로직
     *
     * @return
     */
    @Bean
    MessageListenerAdapter messageListener(RedisMessageSubscriber redisMessageSubscriber) {
        return new MessageListenerAdapter(redisMessageSubscriber);
    }

    /**
     * 발행된 메세지 처리를 위한 리스너를 설정한다.
     *
     * @param redisConnectionFactory
     * @param messageListener
     * @return
     */
    @Bean
    RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter messageListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListener, topic());
        return container;
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("MESSAGE");
    }
}