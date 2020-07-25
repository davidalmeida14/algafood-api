package com.algaworks.algafood.api.assembler.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterDisassembler;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoDisassemblerImpl implements ConverterDisassembler<FormaPagamentoInput,FormaPagamento>{

	@Autowired
	private ModelMapper modelMapper;
	@Override
	public FormaPagamento toDomainObject(FormaPagamentoInput input) {
		return modelMapper.map(input, FormaPagamento.class);
	}

	@Override
	public void copyToDomainObject(FormaPagamentoInput input, FormaPagamento destination) {
		modelMapper.map(input, destination);		
	}

}
