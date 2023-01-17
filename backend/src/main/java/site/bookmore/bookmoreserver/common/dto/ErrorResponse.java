package site.bookmore.bookmoreserver.common.dto;

import lombok.Getter;
import site.bookmore.bookmoreserver.common.exception.ErrorCode;

@Getter
public class ErrorResponse {
    private final String errorCode;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.message = errorCode.getMessage();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }
}
