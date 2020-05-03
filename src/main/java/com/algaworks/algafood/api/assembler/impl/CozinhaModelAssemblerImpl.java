package com.algaworks.algafood.api.assembler.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterAssembler;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaModelAssemblerImpl implements ConverterAssembler<CozinhaModel, Cozinha> {

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CozinhaModel toModel(Cozinha source) {
		return modelMapper.map(source, CozinhaModel.class);
	}

	@Override
	public List<CozinhaModel> toCollectionModel(List<Cozinha> source) {
		return source.stream()
			  .map(cozinha -> toModel(cozinha))
			  .collect(Collectors.toList());
	}
	
	

}
