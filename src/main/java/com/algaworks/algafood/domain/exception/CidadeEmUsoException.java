package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

public class CidadeEmUsoException extends EntidadeEmUsoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CidadeEmUsoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}

	public CidadeEmUsoException(Long id) {
		this(String.format(Constantes.MSG_CIDADE_EM_USO, id));
	}
	
}
