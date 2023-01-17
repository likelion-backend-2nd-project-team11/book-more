package site.bookmore.bookmore.common.exception.not_found;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.REVIEW_NOT_FOUND;

public class ReviewNotFoundException extends AbstractAppException {
    public ReviewNotFoundException() {
        super(REVIEW_NOT_FOUND);
    }
}
