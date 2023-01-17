package site.bookmore.bookmore.common.exception.request_timeout;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.API_REQUEST_TIMEOUT;

public class APIRequestTimeoutException extends AbstractAppException {
    public APIRequestTimeoutException() {
        super(API_REQUEST_TIMEOUT);
    }
}
