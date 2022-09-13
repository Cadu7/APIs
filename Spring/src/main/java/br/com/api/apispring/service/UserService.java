package br.com.api.apispring.service;

import br.com.api.apispring.domain.dto.UserDTO;
import br.com.api.apispring.domain.interfaces.IUserService;
import br.com.api.apispring.domain.model.User;
import br.com.api.apispring.infra.UserRepository;
import br.com.api.apispring.web.exception.ApiError;
import br.com.api.apispring.web.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<UserDTO> getAll(Pageable pageable) {
        return userRepository.pageAll(pageable).stream().map(UserDTO::new).collect(Collectors.toList());
    }
    
    @Override
    public UserDTO getById(UUID id) throws InvalidRequestException {
        User user = getUser(id);
        return new UserDTO(user);
    }
    
    @Override
    public void persist(User user) throws InvalidRequestException {
        user.validate();
        user.setUpToPersist();
        userRepository.persist(user);
    }
    
    @Override
    public void deleteById(UUID id) throws InvalidRequestException {
        getUser(id);
        userRepository.removeById(id);
    }
    
    @Override
    public void updateUser(UUID id, User user) throws InvalidRequestException {
        User userDataBase = getUser(id);
        
        boolean updateName = user.getName() != null || !user.getName().isBlank();
        boolean updateUserName = user.getUserName() != null || !user.getUserName().isBlank();
        boolean updatePassword = user.getPassword() != null || !user.getPassword().isBlank();
        
        if (updateName){
            userDataBase.setName(user.getName());
        }
        
        if (updateUserName){
            userDataBase.setUserName(user.getUserName());
        }
        
        if (updatePassword){
            userDataBase.setPassword(user.getPassword());
        }
        
        userRepository.persist(userDataBase);
    
    }
    
    
    private User getUser(UUID id) throws InvalidRequestException {
        return userRepository.getUserById(id)
                .orElseThrow(() ->
                        new InvalidRequestException(new ApiError("Usuario não encontrado", "Usuario com o id " + id + " não foi encontrado"))
                );
        
    }
    
}
