package mutsa.api.config.security.handler;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mutsa.api.config.jwt.JwtConfig;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.api.dto.LoginResponseDto;
import mutsa.api.util.CookieUtil;
import mutsa.api.util.JwtTokenProvider;
import mutsa.common.repository.redis.RefreshTokenRedisRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtConfig jwtConfig;
    private final RefreshTokenRedisRepository repository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomPrincipalDetails user = (CustomPrincipalDetails) authentication.getPrincipal();

        Algorithm algorithm = jwtConfig.getEncodedSecretKey();
        String accessToken = JwtTokenProvider.createAccessToken(request,
                user.getUsername(), jwtConfig.getAccessTokenExpire(), algorithm,
                user.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

        String refreshToken = JwtTokenProvider.createRefreshToken(request, user.getUsername(), algorithm);
        repository.setRefreshToken(user.getUsername(), refreshToken);

        LoginResponseDto body = new LoginResponseDto();
        body.setUserId(user.getApiId());
        body.setAccessToken(accessToken);

        ResponseCookie cookie = CookieUtil.createCookie(refreshToken);

        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain,
                authentication);
    }
}
