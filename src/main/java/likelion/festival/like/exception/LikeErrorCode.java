package likelion.festival.like.exception;

import likelion.festival.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum LikeErrorCode implements ErrorCode {

    ALREADY_LIKED_BOOTH(HttpStatus.BAD_REQUEST, 3001, "이미 좋아요 한 부스입니다."),
    NOT_LIKED_BOOTH(HttpStatus.BAD_REQUEST, 3002, "좋아요 하지 않는 부스입니다.");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String errorMessage;

    LikeErrorCode(HttpStatus httpStatus, int errorCode, String errorMessage) {
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
