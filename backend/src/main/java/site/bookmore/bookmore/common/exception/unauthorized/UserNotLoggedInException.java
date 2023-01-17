package site.bookmore.bookmore.common.exception.unauthorized;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.USER_NOT_LOGGED_IN;

public class UserNotLoggedInException extends AbstractAppException {
    public UserNotLoggedInException() {
        super(USER_NOT_LOGGED_IN);
    }
}
