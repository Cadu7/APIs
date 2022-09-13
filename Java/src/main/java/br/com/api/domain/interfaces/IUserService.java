package br.com.api.domain.interfaces;

import br.com.api.domain.model.User;
import br.com.api.domain.dto.UserDTO;
import br.com.api.web.exception.InvalidRequestException;

public interface IUserService {
    
    UserDTO getById(Long id) throws InvalidRequestException;
    
    void persist(User user) throws InvalidRequestException;
    
    void deleteById(Long id) throws InvalidRequestException;
    
    UserDTO update(User user, Long id) throws InvalidRequestException;
    
}
