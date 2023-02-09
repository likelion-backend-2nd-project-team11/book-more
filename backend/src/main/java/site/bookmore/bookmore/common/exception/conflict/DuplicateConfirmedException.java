package site.bookmore.bookmore.common.exception.conflict;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.DUPLICATED_CONFIRMED;

public class DuplicateConfirmedException extends AbstractAppException {
    public DuplicateConfirmedException() {
        super(DUPLICATED_CONFIRMED);
    }
}