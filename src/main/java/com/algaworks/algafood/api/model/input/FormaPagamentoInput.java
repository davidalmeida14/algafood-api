package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FormaPagamentoInput {

	@NotNull
	private String descricao;
	
}
