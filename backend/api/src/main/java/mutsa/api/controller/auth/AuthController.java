package mutsa.api.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.LoginResponseDto;
import mutsa.api.dto.auth.AccessTokenResponse;
import mutsa.api.dto.auth.LoginRequest;
import mutsa.api.service.user.UserService;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(HttpServletRequest request, HttpServletResponse response, @Validated @RequestBody LoginRequest loginRequest) throws IOException {
        LoginResponseDto login = userService.login(request, response, loginRequest);
        return ResponseEntity.ok(login);
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

        String refreshToken = userService.refreshToken(request);
        AccessTokenResponse accessTokenResponse = userService.validateRefreshTokenAndCreateAccessToken(refreshToken, request);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        return accessTokenResponse;
    }

}
