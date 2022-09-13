package br.com.api.apispring.security;

import br.com.api.apispring.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetail(userRepository
                .findByUserName(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Login '" + username + "' não encontrado!"))
        );
    }
    
}
