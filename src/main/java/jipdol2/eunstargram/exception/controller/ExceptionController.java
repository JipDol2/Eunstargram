package jipdol2.eunstargram.exception.controller;

import jipdol2.eunstargram.exception.BaseException;
import jipdol2.eunstargram.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> jipDol2Exception(BaseException e){

        int statusCode = e.getStatusCode();

        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidRequestHandler(MethodArgumentNotValidException e){

        int statusCode = HttpStatus.BAD_REQUEST.value();
        String message = HttpStatus.BAD_REQUEST.getReasonPhrase();

        Map<String,String> validation = new HashMap<>();
        for(FieldError fieldError : e.getFieldErrors()){
            validation.put(fieldError.getField(),fieldError.getDefaultMessage());
        }

        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(message)
                .validation(validation)
                .build();

        return ResponseEntity.status(Integer.parseInt(String.valueOf(statusCode))).body(response);
    }
}