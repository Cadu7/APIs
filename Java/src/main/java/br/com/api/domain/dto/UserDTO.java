package br.com.api.domain.dto;

import br.com.api.domain.model.User;

public class UserDTO {
    
    private String name;
    
    public UserDTO(User user) {
        this.name = user.getName();
    }
}
