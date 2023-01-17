package site.bookmore.bookmoreserver.common.exception.not_found;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.REVIEW_NOT_FOUND;

public class ReviewNotFoundException extends AbstractAppException {
    public ReviewNotFoundException() {
        super(REVIEW_NOT_FOUND);
    }
}
