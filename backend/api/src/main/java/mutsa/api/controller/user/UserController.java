package mutsa.api.controller.user;

import jakarta.validation.Valid;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.user.SignUpUserDto;
import mutsa.api.service.user.UserService;
import mutsa.api.util.UserUtil;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody @Valid SignUpUserDto signUpDto) {
        if(UserUtil.diffPassword(signUpDto.getPassword(), signUpDto.getCheckPassword())){
            throw new BusinessException(ErrorCode.DIFFERENT_PASSWORD);
        }
        userService.signUp(signUpDto);
        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.NO_CONTENT);
    }
}
