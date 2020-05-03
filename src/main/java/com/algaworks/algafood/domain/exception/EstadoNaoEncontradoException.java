package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2049513939999482289L;

	public EstadoNaoEncontradoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	
	public EstadoNaoEncontradoException(Long idEstado) {
		this(String.format(Constantes.MSG_ESTADO_NAO_ENCONTRADO, idEstado));
	}
}
