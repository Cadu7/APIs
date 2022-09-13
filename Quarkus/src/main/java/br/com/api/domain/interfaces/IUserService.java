package br.com.api.domain.interfaces;

import br.com.api.domain.model.Metadata;
import br.com.api.domain.model.Page;
import br.com.api.domain.model.User;
import br.com.api.domain.model.UserDTO;
import br.com.api.web.exception.InvalidRequestException;

import java.util.List;

public interface IUserService {
    
    void updateUserById(String userId, User user) throws InvalidRequestException;
    
    void deleteUserById(String userId) throws InvalidRequestException;
    
    void create(User user) throws InvalidRequestException;
    
    Page<UserDTO, Metadata> getAll(Integer page, Integer size) throws InvalidRequestException;
    
    UserDTO getById(String userId) throws InvalidRequestException;
}
