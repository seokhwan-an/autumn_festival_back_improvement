package likelion.festival.global.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public class CommonExceptionResponse {

    private int code;
    private String message;

    public static ResponseEntity<CommonExceptionResponse> toResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus().value())
            .body(new CommonExceptionResponse(errorCode.getCode(), errorCode.getMessage()));
    }
}
