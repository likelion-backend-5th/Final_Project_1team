package mutsa.api.config.socket;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.config.jwt.JwtConfig;
import mutsa.api.util.JwtUtil;
import mutsa.common.exception.ErrorCode;
import mutsa.common.exception.ErrorResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtConfig jwtConfig;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("{}", message.getHeaders());
        log.info("{}",message.getPayload());
        byte[] payloadBytes = (byte[]) message.getPayload();
        String payloadString = new String(payloadBytes, StandardCharsets.UTF_8);
        log.info("JSON Payload: {}", payloadString);


//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        String token = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7);
//        //TODO 토큰의 유효성 검증 해당 부분을 따로 추출하는 방법을 확인
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
//            JwtUtil.JWTInfo jwtInfo = null;
//            try {
//                jwtInfo = JwtUtil.decodeToken(algorithm, token);
//                log.debug(jwtInfo.toString());
//            } catch (TokenExpiredException e) {
//                log.debug("TokenExpiredException: ", e);
//            } catch (JWTVerificationException ignored) {
//                log.debug("JWTVerificationException: ", ignored);
//            }
//
//        }
        return message;
    }
}
