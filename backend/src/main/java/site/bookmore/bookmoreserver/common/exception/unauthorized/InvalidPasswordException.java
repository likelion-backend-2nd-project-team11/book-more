package site.bookmore.bookmoreserver.common.exception.unauthorized;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.INVALID_PASSWORD;

public class InvalidPasswordException extends AbstractAppException {
    public InvalidPasswordException() {
        super(INVALID_PASSWORD);
    }
}
