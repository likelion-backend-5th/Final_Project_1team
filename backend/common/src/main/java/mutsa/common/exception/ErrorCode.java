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
    UNKNOWN_ROLE(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), "G001", "알수없는 ROLE 입니다."),
    // auth

    ACCESS_TOKEN_EXPIRED(HttpStatus.FORBIDDEN.value(), "", "토큰이 만료되었습니다."),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "USER_NOT_FOUND", "해당 유저를 찾을 수 없습니다."),
    DUPLICATION_USER(HttpStatus.NOT_FOUND.value(), "U001", "중복된 ID입니다."),
    DIFFERENT_PASSWORD(HttpStatus.NOT_FOUND.value(), "U002", "입력된 비밀번호가 다릅니다."),

    //article
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "ARTICLE_NOT_FOUND", "해당 게시글을 찾을 수 없습니다."),
    ARTICLE_USER_NOT_MATCH(HttpStatus.BAD_REQUEST.value(), "ARTICLE_USER_NOT_MATCH", "게시글 작성자가 아닙니다."),
    INVALID_ARTICLE_ORDER(HttpStatus.BAD_REQUEST.value(), "", "url path가 부정확합니다."),
    ARTICLE_PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "", "판매자의 권한이 아닙니다."),

    //order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "ORDER_NOT_FOUND", "해당 주문을 찾을 수 없습니다."),
    ORDER_PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "", "판매자나 구매자가 아닙니다."),

    // report
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REPORT_NOT_FOUND", "해당 신고를 찾을 수 없습니다."),

    // review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REVIEW_NOT_FOUND", "해당 리뷰를 찾을 수 없습니다."),
    REVIEW_NOT_ALLOW(HttpStatus.BAD_REQUEST.value(), "REVIEW_NOT_ALLOW", "아직 리뷰를 작성할 수 없습니다."),
    REVIEW_PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "REVIEW_PERMISSION_DENIED", "주문 작성자가 아닙니다.")

    ;

    private final int status;
    private final String code;
    private final String message;
}
