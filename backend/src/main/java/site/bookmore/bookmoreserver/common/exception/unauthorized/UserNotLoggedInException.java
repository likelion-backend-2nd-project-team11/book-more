package site.bookmore.bookmoreserver.common.exception.unauthorized;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.USER_NOT_LOGGED_IN;

public class UserNotLoggedInException extends AbstractAppException {
    public UserNotLoggedInException() {
        super(USER_NOT_LOGGED_IN);
    }
}
