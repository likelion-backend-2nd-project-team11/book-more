package site.bookmore.bookmore.common.exception.conflict;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.DUPLICATED_UNFOLLOW;

public class DuplicateUnfollowException extends AbstractAppException {
    public DuplicateUnfollowException() {
        super(DUPLICATED_UNFOLLOW);
    }
}
