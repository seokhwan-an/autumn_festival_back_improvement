package likelion.festival.menu.exception;

import likelion.festival.global.exception.CommonException;
import likelion.festival.global.exception.ErrorCode;

public class MenuException extends CommonException {

    public MenuException(ErrorCode errorCode) {
        super(errorCode);
    }
}
