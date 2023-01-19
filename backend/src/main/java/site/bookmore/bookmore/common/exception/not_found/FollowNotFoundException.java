package site.bookmore.bookmore.common.exception.not_found;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.FOLLOW_NOT_FOUND;

public class FollowNotFoundException extends AbstractAppException {
    public FollowNotFoundException() {
        super(FOLLOW_NOT_FOUND);
    }
}
