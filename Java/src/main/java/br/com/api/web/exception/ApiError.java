package br.com.api.web.exception;

public class ApiError {
    
    private String errorMessage;
    private String errorDetail;
    
    public ApiError(String errorMessage, String errorDetail) {
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }
}
