package site.bookmore.bookmoreserver.common.exception.not_found;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.BOOK_NOT_FOUND;

public class BookNotFoundException extends AbstractAppException {
    public BookNotFoundException() {
        super(BOOK_NOT_FOUND);
    }
}
