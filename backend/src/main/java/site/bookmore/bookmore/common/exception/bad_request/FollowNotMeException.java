package site.bookmore.bookmore.common.exception.bad_request;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.FOLLOW_NOT_ME;

public class FollowNotMeException extends AbstractAppException {
    public FollowNotMeException() {
        super(FOLLOW_NOT_ME);
    }
}
