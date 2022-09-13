package br.com.api.web.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InvalidAuthenticationException extends RuntimeException {
    
    private final List<ApiError> errors = new ArrayList<>();
    
    public InvalidAuthenticationException(List<ApiError> errors) {
        this.errors.addAll(errors);
    }
    
    public InvalidAuthenticationException(ApiError error) {
        this.errors.add(error);
    }
    
}
