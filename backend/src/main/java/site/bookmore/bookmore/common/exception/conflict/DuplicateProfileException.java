package site.bookmore.bookmore.common.exception.conflict;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.DUPLICATED_PROFILE;

public class DuplicateProfileException extends AbstractAppException {
    public DuplicateProfileException() {
        super(DUPLICATED_PROFILE);
    }
}