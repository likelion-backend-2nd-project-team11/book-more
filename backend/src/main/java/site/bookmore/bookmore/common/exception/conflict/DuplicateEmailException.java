package site.bookmore.bookmore.common.exception.conflict;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.DUPLICATED_EMAIL;

public class DuplicateEmailException extends AbstractAppException {
    public DuplicateEmailException() {
        super(DUPLICATED_EMAIL);
    }
}
