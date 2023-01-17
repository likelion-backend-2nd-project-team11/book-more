package site.bookmore.bookmore.common.exception.internal_server_error;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.DATABASE_ERROR;

public class DatabaseException extends AbstractAppException {
    public DatabaseException() {
        super(DATABASE_ERROR);
    }
}
