package br.com.api.apispring.domain.dto;

import br.com.api.apispring.domain.model.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    
    private String cpf;
    private String name;
    private List<MusicDTO> favorites;
    
    public UserDTO(User user) {
        this.cpf = user.getCpf();
        this.name = user.getName();
        this.favorites = user.getFavorites().parallelStream().map(MusicDTO::new).collect(Collectors.toList());
    }
}
