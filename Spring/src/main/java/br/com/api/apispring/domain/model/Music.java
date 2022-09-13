package br.com.api.apispring.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Music {
    
    @Id
    private UUID id;
    private String name;
    private String album;
    private String author;
    private LocalDateTime release;
    
}
