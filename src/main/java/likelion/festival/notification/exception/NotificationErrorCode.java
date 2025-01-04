package likelion.festival.notification.exception;

import likelion.festival.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum NotificationErrorCode implements ErrorCode {

    NOT_FOUND_NOTIFICATION(HttpStatus.BAD_REQUEST, 5001, "존재하지 않는 공지사항입니다.");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String errorMessage;

    NotificationErrorCode(HttpStatus httpStatus, int errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public int getCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
