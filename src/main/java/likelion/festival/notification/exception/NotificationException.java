package likelion.festival.notification.exception;

import likelion.festival.global.exception.CommonException;
import likelion.festival.global.exception.ErrorCode;

public class NotificationException extends CommonException {

    public NotificationException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
