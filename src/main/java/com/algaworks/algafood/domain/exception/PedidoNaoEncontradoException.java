package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PedidoNaoEncontradoException(String codigo) {
		super(String.format(Constantes.MSG_PEDIDO_NAO_ENCONTRADO, codigo));
	}

}
