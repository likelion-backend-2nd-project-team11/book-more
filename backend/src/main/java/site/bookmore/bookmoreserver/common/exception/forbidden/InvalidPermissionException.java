package site.bookmore.bookmoreserver.common.exception.forbidden;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.INVALID_PERMISSION;

public class InvalidPermissionException extends AbstractAppException {
    public InvalidPermissionException() {
        super(INVALID_PERMISSION);
    }
}
