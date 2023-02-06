package site.bookmore.bookmore.common.exception.bad_request;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.FILE_NOT_EXISTS;

public class FileNotExistsException extends AbstractAppException {
    public FileNotExistsException() {
        super(FILE_NOT_EXISTS);
    }
}