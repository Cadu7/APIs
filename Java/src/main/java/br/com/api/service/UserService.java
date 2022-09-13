package br.com.api.service;

import br.com.api.config.Log;
import br.com.api.domain.dto.UserDTO;
import br.com.api.domain.interfaces.IUserRepository;
import br.com.api.domain.interfaces.IUserService;
import br.com.api.domain.model.User;
import br.com.api.web.exception.ApiError;
import br.com.api.web.exception.InvalidRequestException;

public class UserService implements IUserService {
    
    IUserRepository userRepository;
    
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDTO getById(Long id) throws InvalidRequestException {
        User user = getUser(id);
        return new UserDTO(user);
    }
    
    @Override
    public void persist(User user) throws InvalidRequestException {
        user.validate();
        user.configure();
        userRepository.persist(user);
        Log.info("Usuario Salvo com sucesso");
    }
    
    @Override
    public void deleteById(Long id) throws InvalidRequestException {
        if (id == null || id < 0) {
            throw new InvalidRequestException(new ApiError("Id nulo", "O id do usuario não foi informado"));
        }
        userRepository.deleteById(id);
    }
    
    @Override
    public UserDTO update(User user, Long id) throws InvalidRequestException {
        User userDB = getUser(id);
        boolean hasChange = false;
        
        String name = user.getName();
        if (name != null && !name.equals(userDB.getName())) {
            userDB.setName(name);
            hasChange = true;
        }
        
        String password = user.getPassword();
        if (password != null) {
            hasChange = true;
            userDB.setPassword(password);
            userDB.configure();
        }
        
        if (hasChange) {
            userRepository.update(userDB);
        }
        
        return new UserDTO(userDB);
    }
    
    private User getUser(Long id) throws InvalidRequestException {
        if (id == null || id < 0) {
            throw new InvalidRequestException(new ApiError("Id nulo", "O id do usuario não foi informado"));
        }
        Log.info("Buscando usuario");
        User user = userRepository.findById(id);
        if (user == null) {
            Log.info("Usuario não encontrado");
            throw new InvalidRequestException(new ApiError("Úsuario não encontrado", "Úsuario com o id " + id + " não foi encontrado"));
        }
        return user;
    }
    
}
