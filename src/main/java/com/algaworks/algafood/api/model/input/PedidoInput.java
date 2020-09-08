package com.algaworks.algafood.api.model.input;

import com.algaworks.algafood.api.model.RestauranteResumoModel;
import lombok.Data;
import org.modelmapper.internal.util.Lists;

import java.util.ArrayList;
import java.util.List;

@Data
public class PedidoInput {

    private RestauranteIdInput restaurante;

    private FormaPagamentoIdInput formaPagamento;

    private EnderecoInput enderecoEntrega;

    private List<ItemPedidoInput> itens = new ArrayList<>();
}
