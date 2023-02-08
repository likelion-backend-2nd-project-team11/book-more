package site.bookmore.bookmore.common.exception.not_found;

import site.bookmore.bookmore.common.exception.AbstractAppException;

import static site.bookmore.bookmore.common.exception.ErrorCode.ALARM_NOT_FOUND;

public class AlarmNotFoundException extends AbstractAppException {
    public AlarmNotFoundException() {
        super(ALARM_NOT_FOUND);
    }
}