package com.algaworks.algafood.api.assembler;

public interface ConverterDisassembler<T,D> {
	
	D toDomainObject(T input);
	void copyToDomainObject(T input, D destination);

}
