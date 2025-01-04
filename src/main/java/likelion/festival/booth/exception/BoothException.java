package likelion.festival.booth.exception;

import likelion.festival.global.exception.CommonException;
import likelion.festival.global.exception.ErrorCode;

public class BoothException extends CommonException {

    public BoothException(ErrorCode errorCode) {
        super(errorCode);
    }
}
