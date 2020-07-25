package com.algaworks.algafood.api.assembler.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterDisassembler;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;

@Component
public class GrupoModelDisassemblerImpl implements ConverterDisassembler<GrupoInput, Grupo>{

	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Grupo toDomainObject(GrupoInput input) {
		Grupo grupo = null;
		try {
			grupo = modelMapper.map(input, Grupo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grupo;
	}

	@Override
	public void copyToDomainObject(GrupoInput input, Grupo destination) {
		modelMapper.map(input, destination);
	}


}
