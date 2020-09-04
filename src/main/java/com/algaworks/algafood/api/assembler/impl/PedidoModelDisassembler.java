package com.algaworks.algafood.api.assembler.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterDisassembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoModelDisassembler implements ConverterDisassembler<PedidoModel, Pedido>{

	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Pedido toDomainObject(PedidoModel input) {
		return mapper.map(input, Pedido.class);
	}

	@Override
	public void copyToDomainObject(PedidoModel input, Pedido destination) {
		mapper.map(input, destination);
	}

}
