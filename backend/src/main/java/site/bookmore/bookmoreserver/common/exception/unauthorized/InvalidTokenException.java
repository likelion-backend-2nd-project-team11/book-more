package site.bookmore.bookmoreserver.common.exception.unauthorized;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.INVALID_TOKEN;

public class InvalidTokenException extends AbstractAppException {
    public InvalidTokenException() {
        super(INVALID_TOKEN);
    }
}
