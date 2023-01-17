package site.bookmore.bookmore.common.exception.forbidden;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.INVALID_PERMISSION;

public class InvalidPermissionException extends AbstractAppException {
    public InvalidPermissionException() {
        super(INVALID_PERMISSION);
    }
}
