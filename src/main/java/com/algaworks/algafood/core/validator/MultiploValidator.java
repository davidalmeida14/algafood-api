package com.algaworks.algafood.core.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * Classe responsável por implementar a regra de validação em si.
 * Para isso, é preciso implementar a interface {@link ConstraintValidator}
 * @author davidalmeida
 *
 */
public class MultiploValidator implements ConstraintValidator<Multiplo, Number> {

	private int numeroMultiplo;
	
	@Override
	public void initialize(Multiplo constraintAnnotation) {
		numeroMultiplo = constraintAnnotation.numero();
	}
	
	@Override
	public boolean isValid(Number value, ConstraintValidatorContext context) {
		
		boolean isValido = true;
		
		if(value != null) {
			
			BigDecimal valor = BigDecimal.valueOf(value.doubleValue());
			BigDecimal multiplicador = BigDecimal.valueOf(this.numeroMultiplo);
			
			BigDecimal remainder = valor.remainder(multiplicador);
			isValido = BigDecimal.ZERO.compareTo(remainder) == 0;
			
		}
		return isValido;
	}

}
