package com.algaworks.algafood.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ExceptionUtil {
	
	@Autowired
	private MessageSource messageSource;

	public String recuperarMensagemErroPorId(Long id, String keyMessage) {
		return getMessage(id, keyMessage);
	}
	
	private String getMessage(Long id, String keyMessage) {
		return messageSource.getMessage(keyMessage, new Object[] {id}, LocaleContextHolder.getLocale());
	}

}
