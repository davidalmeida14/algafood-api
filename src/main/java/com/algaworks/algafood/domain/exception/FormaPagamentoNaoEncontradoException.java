package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

public class FormaPagamentoNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FormaPagamentoNaoEncontradoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	
	public FormaPagamentoNaoEncontradoException(Long id) {
		super(String.format(Constantes.MSG_FORMA_PAGAMENTO_NAO_ENONTRADO, id));
	}

}
