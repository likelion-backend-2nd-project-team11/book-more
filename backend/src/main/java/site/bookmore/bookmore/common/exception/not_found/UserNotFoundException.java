package site.bookmore.bookmore.common.exception.not_found;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.USER_NOT_FOUND;

public class UserNotFoundException extends AbstractAppException {
    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }
}
