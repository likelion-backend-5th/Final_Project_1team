package mutsa.api.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.LoginResponseDto;
import mutsa.api.dto.auth.AccessTokenResponse;
import mutsa.api.dto.auth.LoginRequest;
import mutsa.api.service.user.UserService;
import mutsa.api.util.JwtTokenProvider;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private UserService userService;

    @PostMapping("/auth/login")
    public LoginResponseDto login(@Validated @RequestBody LoginRequest loginRequest) {
        throw new IllegalStateException("this method shouldn't be call");
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 만료된 accessToken 재발급 기능
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/auth/token/refresh")
    public AccessTokenResponse reIssuerAccessToken(HttpServletRequest request,
        HttpServletResponse response) {
        if (request.getCookies() == null) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_IN_COOKIE);
        }

        String refreshToken = Arrays.stream(request.getCookies())
            .filter(JwtTokenProvider::isCookieNameRefreshToken)
            .map(Cookie::getValue)
            .findFirst()
            .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_IN_COOKIE));

        return userService.validateRefreshTokenAndCreateAccessToken(refreshToken,
            request);
    }

}
