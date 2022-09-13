package br.com.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    
    private String cpf;
    private String name;
    private String userName;
    
    public UserDTO(User user) {
        this.cpf = user.getCpf();
        this.name = user.getName();
        this.userName = user.getUserName();
    }
}
