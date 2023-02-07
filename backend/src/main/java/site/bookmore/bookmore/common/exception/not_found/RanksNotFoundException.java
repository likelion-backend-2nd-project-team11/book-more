package site.bookmore.bookmore.common.exception.not_found;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.RANKS_NOT_FOUND;

public class RanksNotFoundException extends AbstractAppException {
    public RanksNotFoundException() {
        super(RANKS_NOT_FOUND);
    }
}
