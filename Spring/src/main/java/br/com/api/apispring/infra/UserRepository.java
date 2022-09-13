package br.com.api.apispring.infra;

import br.com.api.apispring.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    default Page<User> pageAll(Pageable pageable) {
        return findAll(pageable);
    }
    
    default void persist(User user) {
        save(user);
    }
    
    default Optional<User> getUserById(UUID id) {
        return findById(id);
    }
    
    default void removeById(UUID id) {
        deleteById(id);
    }
    
    Optional<User> findByUserName(String username);
}
