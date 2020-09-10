package com.algaworks.algafood.api.model.input;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PedidoInput {

	@Valid
	@NotNull
    private RestauranteIdInput restaurante;

	@Valid
	@NotNull
    private FormaPagamentoIdInput formaPagamento;

	@Valid
	@NotNull
    private EnderecoInput enderecoEntrega;

	@Valid
	@NotNull
	@Size(min = 1)
    private List<ItemPedidoInput> itens = new ArrayList<>();
}
