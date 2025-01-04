package likelion.festival.comment.exception;

import likelion.festival.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum CommentErrorCode implements ErrorCode {

    NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, 2001, "존재하지 않는 댓글입니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, 2002, "댓글의 비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String errorMessage;

    CommentErrorCode(HttpStatus httpStatus, int errorCode, String errorMessage) {
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
