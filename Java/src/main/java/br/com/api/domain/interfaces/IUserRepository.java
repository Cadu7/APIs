package br.com.api.domain.interfaces;

import br.com.api.domain.model.User;

public interface IUserRepository {
    
    User findById(Long id);
    
    void persist(User user);
    
    void deleteById(Long id);
    
    void update(User user);
    
}
