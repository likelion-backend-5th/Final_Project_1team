package mutsa.api.config.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtConfig {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.accessTokenExpire}")
    private String accessTokenExpire;

    @Value("${jwt.refreshTokenExpire}")
    private String refreshTokenExpire;
}
