package com.algaworks.algafood.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EstadoModel {
	
	@NotNull
	private Long id;
	
	@NotBlank
	private String nome;
	
}
