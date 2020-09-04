package com.algaworks.algafood.api.assembler.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.assembler.ConverterAssembler;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoResumoModelAssemblerImpl implements ConverterAssembler<PedidoResumoModel, Pedido>{

	@Autowired
	private ModelMapper mapper;
	
	public PedidoResumoModel toModel(Pedido pedido) {
		return mapper.map(pedido, PedidoResumoModel.class);
	}

	public List<PedidoResumoModel> toCollectionModel(List<Pedido> pedidos) {
		return pedidos.stream()
			  .map(pedido -> toModel(pedido))
			  .collect(Collectors.toList());
	}
}
