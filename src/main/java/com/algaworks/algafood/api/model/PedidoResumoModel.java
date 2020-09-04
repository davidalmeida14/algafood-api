package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PedidoResumoModel {

	@EqualsAndHashCode.Include
	private long id;

	private BigDecimal subtotal;

	private BigDecimal taxaFrete;

	private BigDecimal valorTotal;
	
	private OffsetDateTime dataCriacao;
	
	private String status;
	
	private RestauranteResumoModel restaurante;

	private UsuarioModel cliente;
	

}
