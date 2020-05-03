package com.algaworks.algafood.core.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;
/**
 *  Classe responsável por validar a anotação {@link ValorZeroIncluiDescricao}.
 * 
 *  Implementa ConstraintValidator onde é do tipo da anotação e do objeto que iremos validar
 *
 * @author davidalmeida
 *
 */
public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object>{

	private String valorField;
	
	private String descricaoField;
	
	private String descricaoObrigatoria;
	
	@Override
	public void initialize(ValorZeroIncluiDescricao constraint) {
		
		this.valorField = constraint.valorField();
		this.descricaoField = constraint.descricaoField();
		this.descricaoObrigatoria = constraint.descricaoObrigatoria();

	}


	@Override
	public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
		
		boolean valido = true;
		
		try {
			BigDecimal valor = (BigDecimal) BeanUtils
													.getPropertyDescriptor(
															objetoValidacao.getClass(), 
															valorField)
													.getReadMethod()
													.invoke(objetoValidacao);
			
			String descricao = (String) BeanUtils
													.getPropertyDescriptor(
															objetoValidacao.getClass(), 
															descricaoField)
													.getReadMethod()
													.invoke(objetoValidacao);
			
			if(valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
				valido = descricao.toLowerCase().contains(descricaoObrigatoria.toLowerCase());
			}
			
			
			return valido;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
		
		
	}

}
