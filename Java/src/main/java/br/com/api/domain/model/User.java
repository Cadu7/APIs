package br.com.api.domain.model;

import br.com.api.web.exception.ApiError;
import br.com.api.web.exception.InvalidRequestException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

public class User {
    
    private Long id;
    private String name;
    private String password;
    
    public void validate() throws InvalidRequestException {
        List<ApiError> errors = new ArrayList<>();
        if (name == null || name.isBlank()){
            errors.add(new ApiError("Nome vazio", "O atributo name não pode ser vazio"));
        }
        if (password == null || password.isBlank()){
            errors.add(new ApiError("Senha vazia", "O atributo password não pode ser vazio"));
        }
        if (!errors.isEmpty()){
            throw new InvalidRequestException(errors);
        }
    }
    
    public void configure() {
        byte[] hash = null;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.password = hash.toString();
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
