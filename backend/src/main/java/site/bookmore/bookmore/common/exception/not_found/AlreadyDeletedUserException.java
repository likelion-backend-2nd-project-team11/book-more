package site.bookmore.bookmore.common.exception.not_found;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.ALREADY_DELETED_USER;

public class AlreadyDeletedUserException extends AbstractAppException {
    public AlreadyDeletedUserException() {
        super(ALREADY_DELETED_USER);
    }
}
