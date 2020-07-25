package com.algaworks.algafood.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GrupoModel {
	
	private Long id;
	private String nome;

}
