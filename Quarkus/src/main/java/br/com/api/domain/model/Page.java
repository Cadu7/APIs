package br.com.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbPropertyOrder;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonbPropertyOrder({"results","metadata"})
public class Page<E,F> {
    
    private List<E> results;
    private F metadata;
    
}
