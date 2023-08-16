package mutsa.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
//  global
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "", "잘못된 입력값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "", "잘못된 타입입니다."),


//  auth
    ACCESS_TOKEN_EXPIRED(HttpStatus.FORBIDDEN.value(), "", "토큰이 만료되었습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
