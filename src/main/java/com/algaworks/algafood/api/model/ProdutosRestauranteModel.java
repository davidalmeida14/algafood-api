package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProdutosRestauranteModel {
	
	@EqualsAndHashCode.Include
	private Long id;
	
	private String nome;
	
	private String descricao;
	
	private BigDecimal preco;
	
	private Boolean ativo;
	
	private Boolean aberto;

}
