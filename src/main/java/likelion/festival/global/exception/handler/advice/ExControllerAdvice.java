package likelion.festival.global.exception.handler.advice;

import likelion.festival.global.exception.ExceptionCode;
import likelion.festival.global.exception.WrongPassword;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongPassword.class)
    public ErrorResult wrongPassword(WrongPassword e) {
        return new ErrorResult(ExceptionCode.WRONG_PASSWORD);
    }

}
