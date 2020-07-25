package com.algaworks.algafood.api.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EnderecoInput {
	
	@NotBlank
	private String cep;
	
	@NotBlank
	private String logradouro;
	
	@NotBlank
	private String numero;

	@NotBlank
	private String complemento;
	
	@NotBlank
	private String bairro;

	@NotNull
	@Valid
	private CidadeIdInput cidade;

}
