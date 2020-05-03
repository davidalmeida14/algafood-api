package com.algaworks.algafood.api.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CidadeModel {

	@NotNull
	private Long id;
	
	@NotBlank
	private String nome;
	
	@Valid
	@NotNull
	private EstadoModel estado;
	
}
