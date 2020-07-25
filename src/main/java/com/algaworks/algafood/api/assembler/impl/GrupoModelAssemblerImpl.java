package com.algaworks.algafood.api.assembler.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterAssembler;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.domain.model.Grupo;
@Component
public class GrupoModelAssemblerImpl implements ConverterAssembler<GrupoModel, Grupo>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public GrupoModel toModel(Grupo source) {
		GrupoModel model = null;
		try {
			model = modelMapper.map(source, GrupoModel.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	@Override
	public List<GrupoModel> toCollectionModel(List<Grupo> source) {
		return source.stream()
			  .map(grupo -> toModel(grupo))
			  .collect(Collectors.toList());
	}

}
