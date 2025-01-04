package likelion.festival.menu.exception;

import likelion.festival.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum MenuErrorCode implements ErrorCode {

    NOT_FOUND_MENU(HttpStatus.BAD_REQUEST, 4001, "존재하지 않는 메뉴입니다.");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String errorMessage;

    MenuErrorCode(HttpStatus httpStatus, int errorCode, String errorMessage) {
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
