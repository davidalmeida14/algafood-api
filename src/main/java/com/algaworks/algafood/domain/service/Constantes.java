package com.algaworks.algafood.domain.service;

import lombok.Data;

@Data
public class Constantes {
	public static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida pois está em uso.";
	public static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe um cadastro de cozinha para o Id %d";
	
	public static final String MSG_ESTADO_NAO_ENCONTRADO = "Estado de nome ou id %s não foi encontrado.";
	public static final String MSG_ESTADO_CIDADE_NAO_INFORMADO = "Estado ou Cidade não informados";
	
	public static final String MSG_CIDADE_NAO_ENCONTRADA = "Cidade %s não encontrada.";
	public static final String MSG_CIDADE_EM_USO = "Cidade %d não pode ser excluída pois está em uso.";
	
	public static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Restaurante de id ou nome %s nao encontrado.";
	public static final String MSG_RESTAURANTE_EM_USO = "Restaurante de id %d está em uso.";
	public static final String MSG_ID_NAO_INFORMADO = "Identificador não informado.";
	
	public static final String MSG_FORMA_PAGAMENTO_NAO_ENONTRADO= "Forma de pagamento de id %s não encontrado";
	public static final String MSG_FORMA_PAGAMENTO_EM_USO = "Forma de pagamento %d não pode ser excluída pois está em uso.";
	public static final String MSG_GRUPO_USO_EXCEPTION = "O grupo: %s já exsite";
	public static final String KEY_GRUPO_NAO_ENCONTRADO = "grupo-nao-encontrado";
	public static final String MSG_USUARIO_EXISTENTE = "Já existe um usuário cadastrado com e-mail: %s";
	
	public static final String MSG_PEDIDO_NAO_ENCONTRADO = "O pedido de id: %s não foi encontrado";
	
	
	
}
