package br.com.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Metadata {
    
    private Integer page;
    private Integer size;
    private Long totalPages;
    
}
