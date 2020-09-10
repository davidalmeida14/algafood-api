package com.algaworks.algafood.api.model.input;

import lombok.Data;

@Data
public class ItemPedidoInput {

    private Long produtoId;
    private Integer quantidade;
    private String observacao;

}
