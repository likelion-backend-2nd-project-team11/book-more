package site.bookmore.bookmoreserver.common.exception.not_found;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.USER_NOT_FOUND;

public class UserNotFoundException extends AbstractAppException {
    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }
}
