package site.bookmore.bookmoreserver.common.exception.conflict;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.DUPLICATED_EMAIL;

public class DuplicateEmailException extends AbstractAppException {
    public DuplicateEmailException() {
        super(DUPLICATED_EMAIL);
    }
}
