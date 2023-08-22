package mutsa.api.config.security.handler;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mutsa.api.config.jwt.JwtConfig;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.api.dto.LoginResponseDto;
import mutsa.api.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtConfig jwtConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        CustomPrincipalDetails user = (CustomPrincipalDetails) authentication.getPrincipal();

        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
        String accessToken = JwtUtil.createToken(request.getRequestURL().toString(),
            user.getUsername(), jwtConfig.getAccessTokenExpire(), algorithm,
            user.getAuthorities().stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        String refreshToken = JwtUtil.createToken(request.getRequestURL().toString(),
            user.getUsername(), jwtConfig.getRefreshTokenExpire(), algorithm);

        LoginResponseDto body = new LoginResponseDto();
        body.setUserId(user.getApiId());
        body.setAccessToken(accessToken);

        ResponseCookie cookie = ResponseCookie.from(JwtUtil.REFRESH_TOKEN,
                refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofDays(15))
            .sameSite("None")
            .build();

        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain,
            authentication);
    }
}
