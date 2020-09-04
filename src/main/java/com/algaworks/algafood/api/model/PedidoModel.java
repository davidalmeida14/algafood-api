package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.algaworks.algafood.domain.model.enums.StatusPedido;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PedidoModel {

	@EqualsAndHashCode.Include
	private long id;

	private BigDecimal subtotal;

	private BigDecimal taxaFrete;

	private BigDecimal valorTotal;
	
	private OffsetDateTime dataCriacao;
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataEntrega;
	
	private StatusPedido status;

	private OffsetDateTime dataCancelamento;

	private FormaPagamentoModel formaPagamento;
	
	private EnderecoModel enderecoEntrega;

	private RestauranteResumoModel restaurante;

	private UsuarioModel cliente;

	private List<ItemPedidoModel> itens;
	

}
