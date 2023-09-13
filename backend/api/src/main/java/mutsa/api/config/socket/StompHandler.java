package mutsa.api.config.socket;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.util.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        byte[] payloadBytes = (byte[]) message.getPayload();
        String payloadString = new String(payloadBytes, StandardCharsets.UTF_8);

//        아직 헤더에 토큰 검증은 진행하지 않습니다.
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (!StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            String token = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization"))
                .substring(7);

            JwtTokenProvider.JWTInfo jwtInfo = null;
            try {
                jwtInfo = jwtTokenProvider.decodeToken(token);
                log.debug(jwtInfo.toString());
            } catch (TokenExpiredException e) {
                log.debug("TokenExpiredException: ", e);
            } catch (JWTVerificationException ignored) {
                log.debug("JWTVerificationException: ", ignored);
            }

            // WebSocket 세션에 사용자 정보 저장
            accessor.getSessionAttributes().put("username", jwtInfo.getUsername());
        }
        return message;
    }
}
