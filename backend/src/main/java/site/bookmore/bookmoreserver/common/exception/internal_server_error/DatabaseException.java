package site.bookmore.bookmoreserver.common.exception.internal_server_error;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.DATABASE_ERROR;

public class DatabaseException extends AbstractAppException {
    public DatabaseException() {
        super(DATABASE_ERROR);
    }
}
