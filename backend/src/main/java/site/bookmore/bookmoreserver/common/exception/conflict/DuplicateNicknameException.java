package site.bookmore.bookmoreserver.common.exception.conflict;

import site.bookmore.bookmoreserver.common.exception.AbstractAppException;

import static site.bookmore.bookmoreserver.common.exception.ErrorCode.DUPLICATED_NICKNAME;

public class DuplicateNicknameException extends AbstractAppException {
    public DuplicateNicknameException() {
        super(DUPLICATED_NICKNAME);
    }
}
