package com.algaworks.algafood.api.assembler;

import java.util.List;

public interface ConverterAssembler<T, D> {

	T toModel(D source);
	List<T> toCollectionModel(List<D> source);
}
