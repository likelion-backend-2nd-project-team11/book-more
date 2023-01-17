package site.bookmore.bookmoreserver.common.exception.request_timeout;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.API_REQUEST_TIMEOUT;

public class APIRequestTimeoutException extends AbstractAppException {
    public APIRequestTimeoutException() {
        super(API_REQUEST_TIMEOUT);
    }
}
