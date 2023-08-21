package mutsa.api.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.LoginResponseDto;
import mutsa.api.dto.auth.LoginRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final UserDetailsService userDetailsService;

    @PostMapping("/auth/login")
    public LoginResponseDto login(@Validated @RequestBody LoginRequest loginRequest) {
        throw new IllegalStateException("this method shouldn't be call");
    }
}
