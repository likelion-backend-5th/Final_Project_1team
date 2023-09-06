package mutsa.api.config.security.handler;

import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.config.jwt.JwtConfig;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.api.util.CookieUtil;
import mutsa.api.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedirectAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtConfig jwtConfig;

    @Value("${frontendUrl}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain,
            authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        //get user details
        CustomPrincipalDetails user = (CustomPrincipalDetails) authentication.getPrincipal();

        boolean createFlag = (boolean) (user.getAttributes().get("create_flag"));

        //token 생성
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
        String accessToken = JwtTokenProvider.createAccessToken(request,
            user.getUsername(),
            jwtConfig.getAccessTokenExpire(), algorithm,
            user.getAuthorities().stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        String refreshToken = JwtTokenProvider.createRefreshToken(request,
            user.getUsername(), algorithm);

        // create cookie
        ResponseCookie cookie = CookieUtil.createCookie(refreshToken);

        response.setStatus(200);
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(
            frontendUrl +
                "?access_token=" + accessToken +
                "&create_flag=" + createFlag +
                "&userId=" + user.getApiId());
    }
}
