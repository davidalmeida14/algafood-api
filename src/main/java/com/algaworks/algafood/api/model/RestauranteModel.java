package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import com.algaworks.algafood.api.model.view.RestauranteResumoView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteModel {

	@JsonView({RestauranteResumoView.Resumo.class, RestauranteResumoView.ApenasIdENome.class})
	private long id;

	@JsonView({RestauranteResumoView.Resumo.class, RestauranteResumoView.ApenasIdENome.class})
	private String nome;

	@JsonView(RestauranteResumoView.Resumo.class)
	private BigDecimal taxaFrete;

	@JsonView(RestauranteResumoView.Resumo.class)
	private CozinhaModel cozinha;
	
	private Boolean aberto;
	
	private Boolean ativo;
	
	private EnderecoModel endereco;
	
}
