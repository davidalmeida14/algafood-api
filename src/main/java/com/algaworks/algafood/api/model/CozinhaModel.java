package com.algaworks.algafood.api.model;

import com.algaworks.algafood.api.model.view.RestauranteResumoView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CozinhaModel {

	@JsonView(RestauranteResumoView.Resumo.class)
	private long id;
	@JsonView(RestauranteResumoView.Resumo.class)
	private String nome;

}
