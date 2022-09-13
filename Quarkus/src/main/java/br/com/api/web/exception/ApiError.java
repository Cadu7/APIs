package br.com.api.web.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
    
    private String errorMessage;
    private String errorDetail;
}


