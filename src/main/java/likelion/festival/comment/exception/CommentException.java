package likelion.festival.comment.exception;

import likelion.festival.global.exception.CommonException;
import likelion.festival.global.exception.ErrorCode;

public class CommentException extends CommonException {

    public CommentException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
