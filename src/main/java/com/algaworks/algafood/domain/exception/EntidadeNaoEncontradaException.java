package com.algaworks.algafood.domain.exception;

public abstract class EntidadeNaoEncontradaException extends NegocioException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1121040781122377512L;

	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	
}
