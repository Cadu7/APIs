package br.com.api.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthExceptionHandler implements ExceptionMapper<AuthException> {
    
    @Override
    public Response toResponse(AuthException e) {
        return Response.status(e.getStatus()).build();
    }
}
