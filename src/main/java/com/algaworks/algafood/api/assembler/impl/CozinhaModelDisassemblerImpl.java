package com.algaworks.algafood.api.assembler.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterDisassembler;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaModelDisassemblerImpl implements ConverterDisassembler<CozinhaInput, Cozinha>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Cozinha toDomainObject(CozinhaInput input) {
		return modelMapper.map(input, Cozinha.class);
	}

	@Override
	public void copyToDomainObject(CozinhaInput input, Cozinha destination) {
		modelMapper.map(input, destination);
	}

}
