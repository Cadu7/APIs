package br.com.api.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class InvalidRequestExceptionHandler implements ExceptionMapper<InvalidRequestException> {
    
    @Override
    public Response toResponse(InvalidRequestException e) {
        return Response.status(BAD_REQUEST).entity(e.getErrors()).build();
    }
    
}
