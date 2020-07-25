package com.algaworks.algafood.api.assembler.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterAssembler;
import com.algaworks.algafood.api.model.ProdutosRestauranteModel;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoAssemblerImpl implements ConverterAssembler<ProdutosRestauranteModel, Produto>{

	@Autowired
	private ModelMapper mapper;
	
	@Override
	public ProdutosRestauranteModel toModel(Produto source) {
		return mapper.map(source, ProdutosRestauranteModel.class);
	}

	@Override
	public List<ProdutosRestauranteModel> toCollectionModel(List<Produto> source) {
		return source.stream()
			  .map(produto -> toModel(produto))
			  .collect(Collectors.toList());
	}

}
