package com.algaworks.algafood.api.assembler.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioAssemblerImpl implements ConverterAssembler<UsuarioModel, Usuario>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UsuarioModel toModel(Usuario source) {
		return modelMapper.map(source, UsuarioModel.class);
	}

	@Override
	public List<UsuarioModel> toCollectionModel(List<Usuario> source) {
		return source.stream()
			   .map(usuario -> toModel(usuario))
			   .collect(Collectors.toList());
	}

}
