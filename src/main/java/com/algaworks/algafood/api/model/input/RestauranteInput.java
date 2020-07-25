package com.algaworks.algafood.api.model.input;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteInput {

	@NotBlank
	private String nome;
	
	@PositiveOrZero
	@NotNull
	private BigDecimal taxaFrete;
	
	private Boolean ativo;
	
	@Valid
	@NotNull
	private CozinhaIdInput cozinha;
	
	@Valid
	private EnderecoInput endereco;
	
}
