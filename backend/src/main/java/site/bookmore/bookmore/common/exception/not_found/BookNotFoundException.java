package site.bookmore.bookmore.common.exception.not_found;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.BOOK_NOT_FOUND;

public class BookNotFoundException extends AbstractAppException {
    public BookNotFoundException() {
        super(BOOK_NOT_FOUND);
    }
}
