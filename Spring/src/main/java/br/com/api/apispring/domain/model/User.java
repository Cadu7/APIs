package br.com.api.apispring.domain.model;

import br.com.api.apispring.web.exception.ApiError;
import br.com.api.apispring.web.exception.InvalidRequestException;
import jdk.jshell.ErroneousSnippet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static br.com.api.apispring.config.Constants.CPF_REGEX;
import static br.com.api.apispring.domain.model.Role.ROLE_USER;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id
    private UUID id;
    private String cpf;
    private String name;
    private String userName;
    private String password;
    private Role role;
    
    @JoinColumn(referencedColumnName = "id")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Music> favorites;
    
    public String getRoleString() {
        return role.toString();
    }
    
    public void validate() throws InvalidRequestException {
        List<ApiError> errors = new ArrayList<>();
        
        boolean cpfIsNull = cpf == null || cpf.isBlank();
        boolean nameIsNull = name == null || name.isBlank();
        boolean userNameIsNull = userName == null || userName.isBlank();
        boolean passwordIsNull = password == null || password.isBlank();
        
        final String ERROR_MESSAGE_NULL = "Atributo está vazio";
        final UnaryOperator<String> getErrorDetailNull = attribute ->String.format("O atributo %s não pode ser nulo e nem vazio", attribute);
        
        if (cpfIsNull){
            errors.add(new ApiError(ERROR_MESSAGE_NULL,getErrorDetailNull.apply("cpf")));
        }else if (!Pattern.matches(CPF_REGEX,cpf)){
            errors.add(new ApiError("Atributo inválido", "O cpf está fora do padrão"));
        }
        
        if (nameIsNull){
            errors.add(new ApiError(ERROR_MESSAGE_NULL,getErrorDetailNull.apply("name")));
        }
        
        if (userNameIsNull){
            errors.add(new ApiError(ERROR_MESSAGE_NULL,getErrorDetailNull.apply("userName")));
        }
        
        if (passwordIsNull) {
            errors.add(new ApiError(ERROR_MESSAGE_NULL, getErrorDetailNull.apply("password")));
        }
        
        if (!errors.isEmpty()){
            throw new InvalidRequestException(errors);
        }
        
    }
    
    private void setUpId() {
        this.id = UUID.randomUUID();
    }
    
    public void setPassword(String password){
        this.password = new BCryptPasswordEncoder().encode(password);
    }
    
    public void setUpToPersist(){
        if (this.role == null){
            this.role = ROLE_USER;
        }
        setUpId();
        setPassword(this.password);
    }
}
