package br.com.api.domain.model;

import lombok.Data;

@Data
public class TokenReturn {
    
    private String token;
    private String type;
    
    public TokenReturn(String token) {
        this.token = token;
        this.type = "Bearer";
    }
}
