package com.algaworks.algafood.core.validator;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {
	
	
	/**
	 * Método responsável por informar ao spring 
	 * que as mensagens de validações do BeanValidator estarão presentes no 
	 * arquivo message.properties que é do Spring.
	 * 
	 * @param messageSource
	 * @return {@link LocalValidatorFactoryBean}
	 */
	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}

}
