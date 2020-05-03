package com.algaworks.algafood.api.assembler.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterDisassembler;
import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoInputDisassemblerImpl implements ConverterDisassembler<EstadoInput, Estado> {

	@Autowired
	private ModelMapper modelMapper;
	
	public Estado toDomainObject(EstadoInput input) {
		return modelMapper.map(input, Estado.class);
	}
	
	public void copyToDomainObject(EstadoInput input, Estado estado) {
		modelMapper.map(input, estado);
	}
}
