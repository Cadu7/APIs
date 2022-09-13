package br.com.api.web.controller;

import br.com.api.domain.dto.UserDTO;
import br.com.api.domain.interfaces.IUserRepository;
import br.com.api.domain.interfaces.IUserService;
import br.com.api.domain.model.BodyResponse;
import br.com.api.domain.model.Response;
import br.com.api.domain.model.User;
import br.com.api.service.UserService;
import br.com.api.web.exception.InvalidRequestException;

public class UserController {
    
    private final IUserService userService;
    
    public UserController(IUserRepository userRepository) {
        userService = new UserService(userRepository);
    }
    
    public Response getUser(Long id) throws InvalidRequestException {
        UserDTO user = userService.getById(id);
        return new Response(200,user);
    }
    
    public Response postUser(User user) throws InvalidRequestException {
        userService.persist(user);
        return new Response(201,new BodyResponse("Usuario criado com sucesso"));
    }
    
    public Response deleteUser(Long id) throws InvalidRequestException {
        userService.deleteById(id);
        return new Response(200,new BodyResponse("Usuario deletedo com sucesso"));
    }
    
    public Response putUser(Long id, User user) throws InvalidRequestException {
        UserDTO userUpdated = userService.update(user, id);
        return new Response(200, userUpdated);
    }
}
