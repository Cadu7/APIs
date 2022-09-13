package br.com.api.domain.model;

import br.com.api.web.exception.ApiError;
import br.com.api.web.exception.InvalidRequestException;
import io.quarkus.elytron.security.common.BcryptUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static br.com.api.config.Constants.CPF_PATTERN;

@Data
@NoArgsConstructor
@Entity(name = "user_table")
public class User {
    
    @Id
    private UUID id;
    private String cpf;
    private String name;
    @Column(name = "user_name")
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    
    public String getRoleString() {
        return role.toString();
    }
    
    public User(UUID id, String cpf, String name, String userName, String password, Role role) {
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.userName = userName;
        this.password = BcryptUtil.bcryptHash(password, 10);
        this.role = role;
    }
    
    public void validate() throws InvalidRequestException {
        List<ApiError> errors = new ArrayList<>();
        
        boolean cpfIsBlank = cpf == null || cpf.isBlank();
        boolean nameIsBlank = name == null || name.isBlank();
        boolean userNameIsBlank = userName == null || userName.isBlank();
        boolean passwordIsBlank = password == null || password.isBlank();
        
        if (userNameIsBlank) {
            errors.add(new ApiError("UserName inválido", "O user name não pode ser vazio"));
        }
        if (nameIsBlank) {
            errors.add(new ApiError("Name inválido", "O atributo name não pode ser vazio"));
        }
        if (passwordIsBlank) {
            errors.add(new ApiError("Senha invalida", "A senha não pode estar nula"));
        }
        
        if (cpfIsBlank) {
            errors.add(new ApiError("CPF inválido", "O cpf não pode ser nulo"));
        } else if (!Pattern.matches(CPF_PATTERN, this.cpf)) {
            errors.add(new ApiError("CPF inválido", "O CPF está fora do padrão"));
        }
        
        if (!errors.isEmpty()) {
            throw new InvalidRequestException(errors);
        }
    }
    
    public void configure() {
        this.cpf = cpf.replace(".","").replace("-","");
        this.id = UUID.randomUUID();
        hashPassword();
    }
    
    public void hashPassword(){
        this.password = BcryptUtil.bcryptHash(password, 10);
    }
}
