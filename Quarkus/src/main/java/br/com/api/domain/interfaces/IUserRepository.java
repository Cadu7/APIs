package br.com.api.domain.interfaces;

import br.com.api.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.ObjLongConsumer;

public interface IUserRepository {
    
    Optional<User> findByUserName(String userName);
    
    void persistOrUpdate(User user);
    
    Optional<User> findById(String userId);
    
    void delete(UUID id);
    
    Optional<User> findByCPF(String cpf);
    
    void getUsersPage(Integer page, Integer size, ObjLongConsumer<List<User>> resultOfQuery);
}
