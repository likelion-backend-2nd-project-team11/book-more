package site.bookmore.bookmore.common.exception.not_found;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.REVIEW_TAG_RELATION_NOT_FOUND;

public class ReviewTagRelationNotFound extends AbstractAppException {
    public ReviewTagRelationNotFound() {
        super(REVIEW_TAG_RELATION_NOT_FOUND);
    }
}
