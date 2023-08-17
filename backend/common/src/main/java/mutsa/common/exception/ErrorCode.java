package mutsa.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // global
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "", "잘못된 입력값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "", "잘못된 타입입니다."),
    SECURITY_CONTEXT_ERROR(HttpStatus.BAD_REQUEST.value(), "", "잘못된 시큐리티내에 정보가 없습니다."),

    // auth
    ACCESS_TOKEN_EXPIRED(HttpStatus.FORBIDDEN.value(), "", "토큰이 만료되었습니다."),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "USER_NOT_FOUND", "해당 유저를 찾을 수 없습니다."),

    //article
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "ARTICLE_NOT_FOUND", "해당 게시글을 찾을 수 없습니다."),

    //order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "ORDER_NOT_FOUND", "해당 주문을 찾을 수 없습니다."),

    // report
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REPORT_NOT_FOUND", "해당 신고를 찾을 수 없습니다."),

    // review,
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REVIEW_NOT_FOUND", "해당 리뷰를 찾을 수 없습니다.")

    ;

    private final int status;
    private final String code;
    private final String message;
}
