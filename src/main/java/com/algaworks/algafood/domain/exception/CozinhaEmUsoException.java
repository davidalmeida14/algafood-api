package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

public class CozinhaEmUsoException extends EntidadeEmUsoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CozinhaEmUsoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}

	public CozinhaEmUsoException(Long id) {
		super(String.format(Constantes.MSG_COZINHA_EM_USO, id));
	}
}
