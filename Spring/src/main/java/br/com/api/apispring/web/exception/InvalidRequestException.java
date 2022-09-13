package br.com.api.apispring.web.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvalidRequestException extends Exception{
    
    private List<ApiError> errors = new ArrayList<>();
    
    public InvalidRequestException(List<ApiError> errors) {
        this.errors.addAll(errors);
    }
    
    public InvalidRequestException(ApiError error) {
        this.errors.add(error);
    }
    
    
    
}
