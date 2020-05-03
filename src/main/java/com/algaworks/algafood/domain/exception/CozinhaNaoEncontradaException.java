package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CozinhaNaoEncontradaException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	
	public CozinhaNaoEncontradaException(Long idCidade) {
		super(String.format(Constantes.MSG_COZINHA_NAO_ENCONTRADA, idCidade));
		// TODO Auto-generated constructor stub
	}

}
