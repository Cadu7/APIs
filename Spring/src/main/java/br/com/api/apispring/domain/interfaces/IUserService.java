package br.com.api.apispring.domain.interfaces;

import br.com.api.apispring.domain.dto.UserDTO;
import br.com.api.apispring.domain.model.User;
import br.com.api.apispring.web.exception.InvalidRequestException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    
    List<UserDTO> getAll(Pageable pageable);
    
    UserDTO getById(UUID id) throws InvalidRequestException;
    
    void persist(User user) throws InvalidRequestException;
    
    void deleteById(UUID id) throws InvalidRequestException;
    
    void updateUser(UUID id, User user) throws InvalidRequestException;
    
}
