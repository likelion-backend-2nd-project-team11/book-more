package site.bookmore.bookmore.common.exception.unauthorized;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.INVALID_FILE_FORMAT;

public class InvalidFileFormatException extends AbstractAppException {
    public InvalidFileFormatException() {
        super(INVALID_FILE_FORMAT);
    }
}
