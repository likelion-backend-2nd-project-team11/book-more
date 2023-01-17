package site.bookmore.bookmore.common.exception.conflict;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.DUPLICATED_NICKNAME;

public class DuplicateNicknameException extends AbstractAppException {
    public DuplicateNicknameException() {
        super(DUPLICATED_NICKNAME);
    }
}
