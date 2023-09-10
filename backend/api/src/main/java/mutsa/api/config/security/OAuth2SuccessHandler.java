package mutsa.api.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.user.SignUpUserDto;
import mutsa.api.service.user.CustomUserDetailsService;
import mutsa.api.service.user.UserService;
import mutsa.api.util.CookieUtil;
import mutsa.api.util.JwtTokenProvider;
import mutsa.common.repository.redis.RefreshTokenRedisRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REDIRECT_URL_FORMAT = "http://localhost:3000/oauth2-redirect?token=%s"; //프론트리다이렉트 주소
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    // 인증 성공시 호출되는 메소드
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // OAuth2UserServiceImpl에서 반환한 DefaultOAuth2User가 저장된다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 소셜 로그인을 한 새로운 사용자를 우리의 UserEntity로 전환하기 위한 작업
        // username: Email을 @ 기준으로 나누고, ID Provider(Naver) 같은 값을 추가하여 조치
        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");
        String username = email.split("@")[0];
        String authName = String.format("{%s}%s", provider, username);
        String picture = oAuth2User.getAttribute("picture");

        log.info("oauthName : {} ", authName);
        // 처음으로 소셜 로그인한 사용자를 데이터베이스에 등록
        if (!userService.existUser(username)) {
            userService.signUpAuth(new SignUpUserDto(
                    username,
                    "",
                    "",
                    username,
                    email,
                    "",
                    "",
                    "",
                    ""
            ), authName, picture);
        }

        // 데이터베이스에서 사용자 회수
        CustomPrincipalDetails customPrincipalDetails = customUserDetailsService.loadUserByUsername(username);

        // JWT 생성
        String accessToken = jwtTokenProvider.createAccessToken(request, customPrincipalDetails);

        if (refreshTokenRedisRepository.getRefreshToken(username).isEmpty()) {
            String token = jwtTokenProvider.createRefreshToken(request, username);
            refreshTokenRedisRepository.setRefreshToken(username, token);
        }
        String refreshToken = refreshTokenRedisRepository.getRefreshToken(username).get();

        ResponseCookie cookie = CookieUtil.createCookie(refreshToken);

        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        // 목적지 URL 설정
        // 우리 서비스의 Frontend 구성에 따라 유연하게 대처해야 한다.
        String targetUrl = String.format(
                REDIRECT_URL_FORMAT, accessToken
        );

        log.info("url : {}", targetUrl);
        // 실제 Redirect 응답 생성
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
