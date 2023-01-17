package site.bookmore.bookmore.common.exception.unauthorized;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.INVALID_PASSWORD;

public class InvalidPasswordException extends AbstractAppException {
    public InvalidPasswordException() {
        super(INVALID_PASSWORD);
    }
}
