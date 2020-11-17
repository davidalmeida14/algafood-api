package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@JsonFilter("pedidoFilter")
@Data
@EqualsAndHashCode
public class PedidoResumoModel {

	private String codigo;

	private BigDecimal subtotal;

	private BigDecimal taxaFrete;

	private BigDecimal valorTotal;
	
	private OffsetDateTime dataCriacao;
	
	private String status;
	
	private RestauranteResumoModel restaurante;

	private UsuarioModel cliente;
	

}
