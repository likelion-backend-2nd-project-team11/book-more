package site.bookmore.bookmore.common.exception.unauthorized;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.INVALID_TOKEN;

public class InvalidTokenException extends AbstractAppException {
    public InvalidTokenException() {
        super(INVALID_TOKEN);
    }
}
