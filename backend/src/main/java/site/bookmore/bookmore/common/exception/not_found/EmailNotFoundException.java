package site.bookmore.bookmore.common.exception.not_found;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.EMAIL_NOT_FOUND;

public class EmailNotFoundException extends AbstractAppException {
    public EmailNotFoundException() {
        super(EMAIL_NOT_FOUND);
    }
}
