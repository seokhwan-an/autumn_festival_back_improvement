package likelion.festival.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonExceptionResponse> handleCommonException(final CommonException e) {
        return CommonExceptionResponse.toResponse(e.getErrorCode());
    }
}
