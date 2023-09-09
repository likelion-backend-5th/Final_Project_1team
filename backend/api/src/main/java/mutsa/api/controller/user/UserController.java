package mutsa.api.controller.user;

import jakarta.validation.Valid;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.api.dto.user.PasswordChangeDto;
import mutsa.api.dto.user.SignUpUserDto;
import mutsa.api.service.user.UserService;
import mutsa.api.util.UserUtil;
import mutsa.common.dto.user.UserInfoDto;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody @Valid SignUpUserDto signUpDto) {
        if (UserUtil.diffPassword(signUpDto.getPassword(), signUpDto.getCheckPassword())) {
            throw new BusinessException(ErrorCode.DIFFERENT_PASSWORD);
        }
        userService.signUp(signUpDto);
        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('user.read')")
    public ResponseEntity<UserInfoDto> findUserInfo(@AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(userService.findUserInfo(user.getUsername()),
            HttpStatus.OK);
    }

    @PatchMapping("/password")
    @PreAuthorize("hasAuthority('user.update')")
    public ResponseEntity changePassword(@AuthenticationPrincipal CustomPrincipalDetails user,
        @Validated @RequestBody PasswordChangeDto passwordChangeDto) {
        userService.changePassword(user, passwordChangeDto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
