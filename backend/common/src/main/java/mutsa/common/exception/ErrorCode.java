package mutsa.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // global
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "C001", "잘못된 입력값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "C002", "잘못된 타입입니다."),
    SECURITY_CONTEXT_ERROR(HttpStatus.BAD_REQUEST.value(), "C003", "잘못된 시큐리티내에 정보가 없습니다."),
    UNKNOWN_ROLE(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), "C004", "알수없는 ROLE 입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "C005", " 요청메서드가 허용되지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "C006", "서버에서 오류가 발생했습니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "C007", "권한이 없습니다."),

    // auth
    ACCESS_TOKEN_EXPIRED(HttpStatus.FORBIDDEN.value(), "A001", "토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_IN_COOKIE(HttpStatus.UNAUTHORIZED.value(), "A002", "쿠키에 refresh-token이 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "A003", "쿠키에 저장된 refresh-token이 만료되었거나 인증이 불가능합니다."),
    USER_TOKEN_NOT_AVAILABLE(HttpStatus.UNAUTHORIZED.value(), "A004", "Access token을 재발급 받을 수 없습니다."),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "USER_NOT_FOUND", "해당 유저를 찾을 수 없습니다."),
    DUPLICATION_USER(HttpStatus.NOT_FOUND.value(), "U001", "중복된 ID입니다."),
    DIFFERENT_PASSWORD(HttpStatus.NOT_FOUND.value(), "U002", "입력된 비밀번호가 다릅니다."),
    SAME_PASSOWRD(HttpStatus.NOT_FOUND.value(), "U003", "현재 비밀번호와 새 비밀번호가 같습니다."),

    // article
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "ARTICLE_NOT_FOUND", "해당 게시글을 찾을 수 없습니다."),
    ARTICLE_USER_NOT_MATCH(HttpStatus.BAD_REQUEST.value(), "ARTICLE_USER_NOT_MATCH", "게시글 작성자가 아닙니다."),
    INVALID_ARTICLE_ORDER(HttpStatus.BAD_REQUEST.value(), "", "url path가 부정확합니다."),
    ARTICLE_PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "", "판매자의 권한이 아닙니다."),

    // image
    IMAGE_USER_NOT_MATCH(HttpStatus.BAD_REQUEST.value(), "IMAGE_USER_NOT_MATCH", "이미지를 등록한 유저가 아닙니다."),

    // order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "ORDER_NOT_FOUND", "해당 주문을 찾을 수 없습니다."),
    ORDER_PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "", "판매자나 구매자가 아닙니다."),

    // report
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REPORT_NOT_FOUND", "해당 신고를 찾을 수 없습니다."),
    REPORT_INVALID_RESOURCE_TYPE(HttpStatus.BAD_REQUEST.value(), "REPORT_INVALID_RESOURCE_TYPE", "신고할 수 없는 타입의 기능입니다."),

    // review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REVIEW_NOT_FOUND", "해당 리뷰를 찾을 수 없습니다."),
    REVIEW_NOT_ALLOW(HttpStatus.BAD_REQUEST.value(), "REVIEW_NOT_ALLOW", "아직 리뷰를 작성할 수 없습니다."),
    REVIEW_PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "REVIEW_PERMISSION_DENIED", "주문 작성자가 아닙니다."),

    // payment
    PAYMENT_INVALID_AMOUNT(HttpStatus.BAD_REQUEST.value(), "PAYMENT_INVALID_AMOUNT", "옳지 않은 결제 금액입니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "PAYMENT_NOT_FOUND", "해당 결제 정보를 찾을 수 없습니다."),
    PAYMENT_ALREADY_APPROVED(HttpStatus.BAD_REQUEST.value(), "PAYMENT_ALREADY_APPROVED", "이미 인가된 결제입니다."),

    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "CHAT_NOTFOUND","해당 채팅을 찾지 못했습니다." ),
    INVALID_ROOM_REQUEST(HttpStatus.BAD_REQUEST.value(), "INVALID_REQUEST", "자신의 글에 채팅방을 만들 수 없습니다." ),
    CHATROOM_PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), "CRR001", "접근권한이 없는 채팅방 입니다." );

    private final int status;
    private final String code;
    private final String message;
}
