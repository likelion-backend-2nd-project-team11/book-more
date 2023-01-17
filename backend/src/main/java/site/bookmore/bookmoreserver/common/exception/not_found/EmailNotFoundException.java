package site.bookmore.bookmoreserver.common.exception.not_found;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.EMAIL_NOT_FOUND;

public class EmailNotFoundException extends AbstractAppException {
    public EmailNotFoundException() {
        super(EMAIL_NOT_FOUND);
    }
}
