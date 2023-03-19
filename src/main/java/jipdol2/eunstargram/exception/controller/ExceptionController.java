package jipdol2.eunstargram.exception.controller;

import jipdol2.eunstargram.exception.JipDol2Exception;
import jipdol2.eunstargram.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.NestedServletException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(JipDol2Exception.class)
    public ResponseEntity<ErrorResponse> jipDol2Exception(JipDol2Exception e){

        int statusCode = e.getStatusCode();

        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }

    @ResponseBody
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