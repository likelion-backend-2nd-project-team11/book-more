package site.bookmore.bookmoreserver.common.dto;

import lombok.Getter;
import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

@Getter
public class ResultResponse<T> {
    private static final String SUCCESS = "SUCCESS";
    private static final String ERROR = "ERROR";
    private final String resultCode;
    private final T result;

    public ResultResponse(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public static <T> ResultResponse<T> success(T result) {
        return new ResultResponse<>(SUCCESS, result);
    }

    public static ResultResponse<ErrorResponse> error(ErrorResponse errorResponse) {
        return new ResultResponse<>(ERROR, errorResponse);
    }

    public static ResultResponse<ErrorResponse> error(AbstractAppException e) {
        return new ResultResponse<>(ERROR, ErrorResponse.of(e.getErrorCode()));
    }
}
