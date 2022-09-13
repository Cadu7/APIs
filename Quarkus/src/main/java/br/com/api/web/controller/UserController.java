package br.com.api.web.controller;

import br.com.api.domain.interfaces.IUserService;
import br.com.api.domain.model.Role;
import br.com.api.domain.model.User;
import br.com.api.web.exception.InvalidRequestException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static br.com.api.domain.model.Role.ADMIN;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;


@ApplicationScoped
@Path("/user")
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    
    @Inject
    IUserService userService;
    
    @GET
    @Path("/{userId}")
    public Response getUserById(@PathParam("userId") String userId) throws InvalidRequestException {
        return Response.status(OK).entity(userService.getById(userId)).build();
    }
    
    @GET
    public Response getAllUser(@QueryParam("page") Integer page, @QueryParam("size") Integer size) throws InvalidRequestException {
        return Response.status(OK).entity(userService.getAll(page, size)).build();
    }
    
    @POST
    public Response createUser(User user) throws InvalidRequestException {
        user.setRole(Role.USER);
        userService.create(user);
        return Response.status(CREATED).build();
    }
    
    @POST
    @Path("/admin")
    @RolesAllowed("ADMIN")
    public Response createAdminUser(User user) throws InvalidRequestException {
        user.setRole(ADMIN);
        userService.create(user);
        return Response.status(CREATED).build();
    }
    
    @DELETE
    @Path("/{userId}")
    public Response deleteById(@PathParam("userId") String userId) throws InvalidRequestException {
        userService.deleteUserById(userId);
        return Response.status(OK).build();
    }
    
    @PUT
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") String userId, User user) throws InvalidRequestException {
        userService.updateUserById(userId, user);
        return Response.status(OK).build();
    }
    
}
