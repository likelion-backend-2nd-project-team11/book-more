package site.bookmore.bookmore.common.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {
    INVALID_PASSWORD(UNAUTHORIZED, "잘못된 패스워드입니다."),
    INVALID_TOKEN(UNAUTHORIZED, "잘못된 토큰입니다."),
    USER_NOT_LOGGED_IN(UNAUTHORIZED, "로그인이 필요합니다."),
    INVALID_PERMISSION(FORBIDDEN, "권한이 없습니다."),
    USER_NOT_FOUND(NOT_FOUND, "해당하는 유저를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(NOT_FOUND, "해당하는 이메일을 찾을 수 없습니다."),
    BAD_CONSTANT(BAD_REQUEST, "잘못된 인자입니다."),
    BOOK_NOT_FOUND(NOT_FOUND, "해당하는 책 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "해당하는 리뷰를 찾을 수 없습니다."),
    FOLLOW_NOT_FOUND(NOT_FOUND, "팔로우 중이 아닙니다."),
    API_REQUEST_TIMEOUT(REQUEST_TIMEOUT, "요청 시간이 초과되었습니다."),
    DUPLICATED_NICKNAME(CONFLICT, "이미 사용중인 닉네임입니다."),
    DUPLICATED_PROFILE(CONFLICT, "프로필이 이미 기본 사진입니다."),
    DUPLICATED_EMAIL(CONFLICT, "이미 사용중인 이메일입니다."),
    DUPLICATED_FOLLOW(CONFLICT, "이미 팔로우 중입니다."),
    FOLLOW_NOT_ME(BAD_REQUEST, "나를 팔로우 할 수 없습니다."),
    FILE_NOT_EXISTS(BAD_REQUEST, "파일이 첨부되지 않았습니다."),
    FILE_SIZE_EXCEED(BAD_REQUEST, "업로드 가능한 파일 용량을 초과했습니다."),
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 에러");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
