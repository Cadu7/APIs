package br.com.api.web.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthException extends RuntimeException {
    
    private final int status;
    
}
