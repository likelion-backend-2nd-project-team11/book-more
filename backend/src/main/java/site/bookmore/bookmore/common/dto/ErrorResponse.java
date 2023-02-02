package site.bookmore.bookmore.common.dto;

import lombok.Getter;
import site.bookmore.bookmore.common.exception.ErrorCode;

import java.util.Map;

@Getter
public class ErrorResponse {
    private final String errorCode;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(Map<String, String> result) {

        String key = "";

        for (String keys : result.keySet()) {
            key = keys;
        }

        this.errorCode = key + " 입력값 오류";
        this.message = result.get(key);
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(Map<String, String> result) {
        return new ErrorResponse(result);
    }
}
