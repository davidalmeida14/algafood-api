package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadeEmUsoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6164727756406016422L;

	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}
}
