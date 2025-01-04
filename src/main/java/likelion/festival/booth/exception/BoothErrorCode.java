package likelion.festival.booth.exception;

import likelion.festival.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BoothErrorCode implements ErrorCode {

    NOT_FOUND_BOOTH(HttpStatus.BAD_REQUEST, 1001, "존재하지 않는 부스입니다.");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String errorMessage;

    BoothErrorCode(HttpStatus httpStatus, int errorCode, String errorMessage) {
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
