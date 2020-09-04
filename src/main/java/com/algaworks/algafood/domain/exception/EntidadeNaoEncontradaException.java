package com.algaworks.algafood.domain.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public abstract class EntidadeNaoEncontradaException extends NegocioException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1121040781122377512L;

	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	
}
