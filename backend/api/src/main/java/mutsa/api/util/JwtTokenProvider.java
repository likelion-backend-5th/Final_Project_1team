package mutsa.api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String BEARER = "Bearer ";
    public static final String REFRESH_TOKEN = "refresh_token";
    private static final String AUTHORITIES = "authorities";

    public static String createAccessToken(HttpServletRequest request, String payload,
        String tokenExpire, Algorithm algorithm, Collection<String> authorities) {
        return BEARER + JWT.create()
            .withSubject(payload)
            .withIssuer(request.getRequestURI().toString())
            .withExpiresAt(
                new Date(System.currentTimeMillis() + Duration.ofDays(1).toMillis()))
            .withClaim("authorities",
                new ArrayList<>(authorities))
            .sign(algorithm);
    }

    public static String createAccessToken(HttpServletRequest request, String subject,
        String tokenExpire, Algorithm algorithm) {
        return BEARER + JWT.create()
            .withSubject(subject)
            .withIssuer(request.getRequestURI().toString())
            .withExpiresAt(
                new Date(System.currentTimeMillis() + Integer.parseInt(tokenExpire)))
            .sign(algorithm);
    }

    public static String createRefreshToken(HttpServletRequest request, String subject,
        Algorithm algorithm) {
        return JWT.create()
            .withSubject(subject)
            .withIssuer(request.getRequestURI().toString())
            .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofDays(30).toMillis()))
            .sign(algorithm);
    }

    /**
     * 1. 토큰이 정상적인지 검증(위조, 만료 여부) 2. Access Token인지 Refresh Token인지 구분
     *
     * @param algorithm
     * @param token
     * @return
     * @throws JWTVerificationException
     */
    public static JWTInfo decodeToken(Algorithm algorithm, String token)
        throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();

        String[] authoritiesJWT = decodedJWT.getClaim("authorities")
            .asArray(String.class);

        if (authoritiesJWT != null) {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Arrays.stream(authoritiesJWT).forEach(authority -> {
                authorities.add(new SimpleGrantedAuthority(authority));
            });
        }

        return JWTInfo.builder()
            .username(username)
            .authorities(authoritiesJWT)
            .build();
    }

    public static boolean isCookieNameRefreshToken(Cookie cookie) {
        return JwtTokenProvider.REFRESH_TOKEN.equals(cookie.getName());
    }

    @Getter
    @Builder
    public static class JWTInfo {
        private final String username;
        private final String[] authorities;
    }
}
