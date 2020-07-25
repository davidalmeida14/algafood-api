package com.algaworks.algafood.api.assembler.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterDisassembler;
import com.algaworks.algafood.api.model.input.ProdutoRestauranteInput;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoDisassemblerImpl implements ConverterDisassembler<ProdutoRestauranteInput, Produto>{

	@Autowired
	private ModelMapper modelMapper;
	@Override
	public Produto toDomainObject(ProdutoRestauranteInput input) {
		return modelMapper.map(input, Produto.class);
	}

	@Override
	public void copyToDomainObject(ProdutoRestauranteInput input, Produto destination) {
		modelMapper.map(input, destination);
	}


}
