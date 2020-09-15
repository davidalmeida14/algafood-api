package com.algaworks.algafood.domain.model.enums;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {

	CRIADO("Criado"),
	CONFIRMADO("Confirmado", CRIADO), 
	ENTREGUE("Entregue", CONFIRMADO), 
	CANCELADO("Cancelado", CRIADO, CONFIRMADO);
	
	
	private List<StatusPedido> statusAnteriores;
	
	private String mensagem;
	
	StatusPedido(String mensagem, StatusPedido...pedidosAnteriores) {
		this.mensagem = mensagem;
		this.statusAnteriores = Arrays.asList(pedidosAnteriores);
	}
	
	public String getMensagem() {
		return this.mensagem;
	}
	
	public boolean naoPodeAlterarPara(StatusPedido novoStatus) {
		return !novoStatus.statusAnteriores.contains(this);
	}
	
	
}
