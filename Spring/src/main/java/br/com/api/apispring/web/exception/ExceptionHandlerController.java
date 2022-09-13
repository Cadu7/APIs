package br.com.api.apispring.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ExceptionHandlerController {
    
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handlerInvalidRequest(InvalidRequestException exception){
        return ResponseEntity.status(BAD_REQUEST).body(exception.getErrors());
    }
    
}
