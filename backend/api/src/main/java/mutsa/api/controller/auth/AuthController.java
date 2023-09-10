package mutsa.api.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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
    public AccessTokenResponse reIssuerAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (request.getCookies() == null) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_IN_COOKIE);
        }

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(JwtTokenProvider::isCookieNameRefreshToken)
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_IN_COOKIE));


        return userService.validateRefreshTokenAndCreateAccessToken(refreshToken, request);
    }

}
