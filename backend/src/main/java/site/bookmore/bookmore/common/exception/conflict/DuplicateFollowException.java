package site.bookmore.bookmore.common.exception.conflict;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.DUPLICATED_FOLLOW;

public class DuplicateFollowException extends AbstractAppException {
    public DuplicateFollowException() {
        super(DUPLICATED_FOLLOW);
    }
}
