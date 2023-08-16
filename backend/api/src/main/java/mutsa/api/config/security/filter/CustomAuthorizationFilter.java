package mutsa.api.config.security.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.config.jwt.JwtConfig;
import mutsa.api.util.JwtUtil;
import mutsa.api.util.JwtUtil.JWTInfo;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.ErrorCode;
import mutsa.common.exception.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    private static final String BEARER = "Bearer ";
    private static final String ACCESS_TOKEN_KEY = "accessToken";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            token = getToken(authorizationHeader);
        }

        if (token != null) {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
            JWTInfo jwtInfo = null;
            try {
                jwtInfo = JwtUtil.decodeToken(algorithm, token);
                log.debug(jwtInfo.toString());
            } catch (TokenExpiredException e) {
                log.debug("TokenExpiredException: ", e);
                ErrorResponse er = ErrorResponse.of(ErrorCode.ACCESS_TOKEN_EXPIRED);
                getAccessTokenExpired(response, er);
                return;
            } catch (JWTVerificationException ignored) {
                log.debug("JWTVerificationException: ", ignored);
            }

            if (jwtInfo != null && jwtInfo.getAuthorities() != null) {
                SecurityContextHolder.getContext()
                        .setAuthentication(getAuthenticationToken(jwtInfo));
            }
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(JWTInfo jwtInfo) {
        return new UsernamePasswordAuthenticationToken(
                CustomPrincipalDetails.of(
                        User.of(
                                jwtInfo.getUsername(),
                                null,
                                null,
                                null,
                                null
                        ),
                        null
                ),
                null,
                Arrays.stream(jwtInfo.getAuthorities())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    private static void getAccessTokenExpired(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(errorResponse.getStatus());
        Map<String, String> body = new HashMap<>();
        body.put("message", errorResponse.getMessage());
        body.put("status",Integer.toString(errorResponse.getStatus()));
        body.put("code", errorResponse.getCode());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }

    private String getToken(String authorizationHeader) {
        return authorizationHeader.substring(BEARER.length());
    }

}
