package site.bookmore.bookmore.common.exception.bad_request;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.BAD_CONSTANT;

public class BadConstantException extends AbstractAppException {
    public BadConstantException() {
        super(BAD_CONSTANT);
    }
}
