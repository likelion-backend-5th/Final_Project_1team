package mutsa.api.config.security.handler;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.time.Duration;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedirectAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtConfig jwtConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //get user details
        CustomPrincipalDetails user = (CustomPrincipalDetails) authentication.getPrincipal();

        boolean createFlag = (boolean) (user.getAttributes().get("create_flag"));

        //token 생성
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
        String accessToken = JwtUtil.createToken(request.getRequestURL().toString(), user.getUsername(),
                jwtConfig.getAccessTokenExpire(), algorithm,
                user.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

        String refreshToken = JwtUtil.createToken(request.getRequestURL().toString(), user.getUsername(), jwtConfig.getRefreshTokenExpire(), algorithm);

        // create cookie
        ResponseCookie cookie = ResponseCookie.from(JwtUtil.REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(15))
                .sameSite("None")
                .build();

        LoginResponseDto responseBody = new LoginResponseDto();
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        responseBody.setUserId(user.getApiId());
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        responseBody.setAccessToken(accessToken);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }
}
