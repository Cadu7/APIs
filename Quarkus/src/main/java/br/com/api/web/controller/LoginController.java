package br.com.api.web.controller;

import br.com.api.domain.interfaces.ILoginService;
import br.com.api.domain.model.LoginDto;
import br.com.api.domain.model.TokenReturn;
import io.quarkus.security.identity.SecurityIdentity;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/login")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {
    
    @Inject
    ILoginService service;
    
    @GET
    public Response generateJwtToken(@HeaderParam("Authorization") String token){
        return Response.status(200).entity(new TokenReturn(service.authenticate(token))).build();
    }
    
    
    
}
