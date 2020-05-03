package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Rercurso não encontrado."),
	ERRO_NEGOCIO("/erro-negocio", "Erro de negócio"),
	ERRO_SISTEMA("/erro-de-sistema", "Erro de Sistema"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade está em uso"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-inconpreensivel", "Mensagem incompreensível"),
	PARAMETROS_INVALIDOS("/parametros-invalidos", "Parametros inválidos"),
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos");
	
	private String title;
	private String uri;
	
	private final String url = "http://www.algafood.com";
	
	private ProblemType(String path, String title) {
		this.uri = url + path;
		this.title = title;
	}
}
