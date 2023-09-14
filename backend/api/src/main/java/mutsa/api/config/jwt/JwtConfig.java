package mutsa.api.config.jwt;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

import com.auth0.jwt.algorithms.Algorithm;
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

    public Algorithm getEncodedSecretKey() {
        Algorithm algorithm = HMAC256(this.secretKey.getBytes());
        return algorithm;
    }
}
