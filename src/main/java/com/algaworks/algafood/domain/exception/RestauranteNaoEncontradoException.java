package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RestauranteNaoEncontradoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	
	public RestauranteNaoEncontradoException(Long id) {
		super(String.format(Constantes.MSG_RESTAURANTE_NAO_ENCONTRADO, id));
	}

}
