package br.com.api.service;

import br.com.api.domain.interfaces.IUserRepository;
import br.com.api.domain.interfaces.IUserService;
import br.com.api.domain.model.Metadata;
import br.com.api.domain.model.Page;
import br.com.api.domain.model.User;
import br.com.api.domain.model.UserDTO;
import br.com.api.web.exception.ApiError;
import br.com.api.web.exception.InvalidRequestException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService implements IUserService {
    
    @Inject
    IUserRepository repository;
    
    @Override
    public UserDTO getById(String userId) throws InvalidRequestException {
        return new UserDTO(getUserById(userId));
    }
    
    @Override
    public Page<UserDTO, Metadata> getAll(Integer page, Integer size) throws InvalidRequestException {
        page--;
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size > 100 || size < 0) {
            size = 50;
        }
        
        AtomicReference<List<UserDTO>> results = new AtomicReference<>();
        AtomicReference<Long> pages = new AtomicReference<>();
        repository.getUsersPage(page, size, (result, totalPages) -> {
            results.set(result.parallelStream().map(UserDTO::new).collect(Collectors.toList()));
            pages.set(totalPages);
        });
        
        Page<UserDTO, Metadata> pageResult = new Page<>();
        pageResult.setResults(results.get());
        pageResult.setMetadata(new Metadata(page + 1, size, pages.get()));
        return pageResult;
        
    }
    
    @Override
    public void create(User user) throws InvalidRequestException {
        user.validate();
        verifyIfUserAlreadyExist(user);
        user.configure();
        repository.persistOrUpdate(user);
    }
    
    @Override
    public void deleteUserById(String userId) throws InvalidRequestException {
        User user = getUserById(userId);
        repository.delete(user.getId());
    }
    
    @Override
    public void updateUserById(String userId, User user) throws InvalidRequestException {
        User userDB = getUserById(userId);
    
        String name = user.getName();
        boolean updateName = name != null && !name.isBlank();
        
        String userName = user.getUserName();
        boolean updateUserName = userName != null && !userName.isBlank();
        
        String password = user.getPassword();
        boolean updatePassword = password != null && ! password.isBlank();
        
        if (updateName){
            userDB.setName(name);
        }
        if (updateUserName){
            userDB.setUserName(userName);
        }
        if (updatePassword){
            userDB.setPassword(password);
            userDB.hashPassword();
        }
        
        
        repository.persistOrUpdate(userDB);
    }
    
    
    private User getUserById(String userId) throws InvalidRequestException {
        if (userId == null || userId.isBlank()) {
            throw new InvalidRequestException(new ApiError("User Id não informado", "O id do usuario não foi informado na requisição"));
        }
        return repository.findById(userId).orElseThrow(() ->
                new InvalidRequestException(new ApiError("Úsuario não encontrado", "O usuário com o id " + userId + " não foi encontrado")));
    }
    
    private void verifyIfUserAlreadyExist(User user) throws InvalidRequestException {
        verifyUserName(user.getUserName());
        verifyCPF(user.getCpf());
    }
    
    private void verifyCPF(String cpf) {
        repository.findByCPF(cpf).ifPresent(userDB -> {
            throw new InvalidRequestException(new ApiError("Usuário já existente", "O cpf utilizado já está cadastrado"));
        });
    }
    
    
    private void verifyUserName(String username) {
        repository.findByUserName(username).ifPresent(userDB -> {
            throw new InvalidRequestException(new ApiError("Usuário já existente", "O user name utilizado já está cadastrado"));
        });
    }
    
}
