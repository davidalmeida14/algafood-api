package com.algaworks.algafood.api.assembler.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoAssemblerImpl {

	@Autowired
	private ModelMapper modelMapper;
	
	public FormaPagamentoModel toModel(FormaPagamento source) {
		return modelMapper.map(source, FormaPagamentoModel.class);
	}

	public List<FormaPagamentoModel> toCollectionModel(List<FormaPagamento> source) {
		return source.stream()
			  .map(formapagamento -> toModel(formapagamento))
			  .collect(Collectors.toList());
	}
	
	public List<FormaPagamentoModel> toCollectionModel(Collection <FormaPagamento> source) {
		return source.stream()
			  .map(formapagamento -> toModel(formapagamento))
			  .collect(Collectors.toList());
	}


}
