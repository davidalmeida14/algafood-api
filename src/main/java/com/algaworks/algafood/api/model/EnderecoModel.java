package com.algaworks.algafood.api.model;

import lombok.Data;

@Data
public class EnderecoModel {
	
	private String cep;

	private String logradouro;

	private String numero;

	private String complemento;

	private String bairro;

	private CidadeResumoModel cidade;
}
