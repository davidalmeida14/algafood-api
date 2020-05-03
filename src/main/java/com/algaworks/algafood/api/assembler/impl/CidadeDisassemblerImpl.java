package com.algaworks.algafood.api.assembler.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterDisassembler;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeDisassemblerImpl implements ConverterDisassembler<CidadeInput, Cidade>{

	@Autowired
	private ModelMapper modelMapper;
	
	public Cidade toDomainObject(CidadeInput input) {
		return modelMapper.map(input, Cidade.class);
	}
	
	public void copyToDomainObject(CidadeInput input, Cidade cidade) {
		modelMapper.map(input, cidade);
	}
}
