package br.com.api.web.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvalidRequestException extends RuntimeException {
    
    private List<ApiError> errors = new ArrayList<>();
    
    public InvalidRequestException(ApiError error) {
        errors.add(error);
    }
    
    public InvalidRequestException(List<ApiError> errors) {
        errors.addAll(errors);
    }
    
}
