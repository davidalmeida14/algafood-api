/**
 * 
 */
package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.service.Constantes;

/**
 * @author davidalmeida
 *
 */
public class RestauranteEmUsoException extends EntidadeEmUsoException {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 7430324572693407824L;

	public RestauranteEmUsoException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	public RestauranteEmUsoException(Long id) {
		super(String.format(Constantes.MSG_RESTAURANTE_EM_USO, id));
	}
}
