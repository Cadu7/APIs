package br.com.api.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Response {
    
    private int status;
    private Object body;
    
    public byte[] getJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(this.body);
    }
    
    public Response(int status, Object body) {
        this.status = status;
        this.body = body;
    }
    
    public int getStatus() {
        return status;
    }
}
