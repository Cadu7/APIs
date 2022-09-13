package br.com.api.web.exception;


import java.util.ArrayList;
import java.util.List;

public class InvalidRequestException extends Exception {
    
    private final List<ApiError> errors = new ArrayList<>();
    
    public InvalidRequestException(ApiError error) {
        this.errors.add(error);
    }
    
    public InvalidRequestException(List<ApiError> errors) {
        this.errors.addAll(errors);
    }
    
    public List<ApiError> getErrors() {
        return errors;
    }
}
