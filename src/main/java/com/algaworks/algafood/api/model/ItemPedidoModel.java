
package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ItemPedidoModel {

	@EqualsAndHashCode.Include
	private Long produtoId;
	
	private String produtoNome;
	
	private Integer quantidade;
	
	private BigDecimal precoUnitario;
	
	private BigDecimal precoTotal;
	
	private String observacao;
	
}
