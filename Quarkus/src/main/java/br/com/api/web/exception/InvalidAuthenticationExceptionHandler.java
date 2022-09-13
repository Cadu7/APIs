package br.com.api.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Provider
public class InvalidAuthenticationExceptionHandler implements ExceptionMapper<InvalidAuthenticationException> {
    
    @Override
    public Response toResponse(InvalidAuthenticationException e) {
        return Response.status(UNAUTHORIZED).entity(e.getErrors()).build();
    }
    
}
