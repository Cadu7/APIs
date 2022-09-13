package br.com.api.apispring.web.exception;

import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {
    
    private ApiError apiError;
    
    public AuthException( ApiError apiError) {
        super(apiError.getErrorMessage());
        this.apiError = apiError;
    }
    
    public AuthException(String msg) {
        super(msg);
    }
}
