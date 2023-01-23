package site.bookmore.bookmore.common.exception.bad_request;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.INVALID_EMAIL_FORMAT;

public class InvalidEmailFormatException extends AbstractAppException {
    public InvalidEmailFormatException() {
        super(INVALID_EMAIL_FORMAT);
    }
}
