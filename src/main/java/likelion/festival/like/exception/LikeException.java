package likelion.festival.like.exception;

import likelion.festival.global.exception.CommonException;

public class LikeException extends CommonException {

    public LikeException(LikeErrorCode errorCode) {
        super(errorCode);
    }
}
