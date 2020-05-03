package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	
	public CidadeNaoEncontradaException(Long idCidade) {
		this(String.format(Constantes.MSG_CIDADE_NAO_ENCONTRADA, idCidade));
	}
}
