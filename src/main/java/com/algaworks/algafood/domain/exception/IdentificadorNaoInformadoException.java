package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdentificadorNaoInformadoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7082381079484034975L;

	public IdentificadorNaoInformadoException(String mensagem) {
		super(mensagem);
	}
}
