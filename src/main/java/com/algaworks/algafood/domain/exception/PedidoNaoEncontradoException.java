package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PedidoNaoEncontradoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}

	public PedidoNaoEncontradoException(Long id) {
		super(String.format(Constantes.MSG_PEDIDO_NAO_ENCONTRADO, id));
	}

}
