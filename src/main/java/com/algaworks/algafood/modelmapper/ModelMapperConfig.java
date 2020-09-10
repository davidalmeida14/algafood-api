package com.algaworks.algafood.modelmapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.EnderecoModel;
import com.algaworks.algafood.api.model.input.ItemPedidoInput;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;

@Configuration
public class ModelMapperConfig {

	TypeMap<Endereco, EnderecoModel> typeMap = null;
	TypeMap<ItemPedidoInput, ItemPedido> typeMapPedido;
	
	/**
	 * Registra Bean ModelMapper no contexto do Spring
	 * 
	 * @return {@link ModelMapper}
	 */
	
	@Bean
	public ModelMapper modelMapper() {
		
		ModelMapper modelMapper = new ModelMapper();
		
		/**
		 * Cria o mapeamento da classe endereço para EnderecoModel
		 * 
		 * @params ClasseOrigem -> ClasseDestino
		 */
		this.typeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
		this.typeMapPedido = modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class);
		adicionaMapeamento(typeMap);
		
		return modelMapper;
	}

	/**
	 * Mapeia o nome do estado da fonte (Endereço) para o destino (EnderecoModel)
	 * O primeiro lambda pega o valor a ser preenchido.
	 * O segundo lambda recebe uma instancia do destino e o valor que o primero lambada retornou e aplica 
	 * ao atributo que queremos mapear.
	 * 
	 * Ou seja, pega o nome do estado da entidade Endereco e mapeia para a String estado que está no CidadeResumoModel (dentro de EnderecoModel)
	 * 
	 * 
	 */
	private void adicionaMapeamento(TypeMap<Endereco, EnderecoModel> typeMap) {
		this.typeMap.<String>addMapping(
				src -> src.getCidade().getEstado().getNome(), 
				(dest, value) -> dest.getCidade().setEstado(value));
		this.typeMapPedido.addMappings(mapper -> mapper.skip(ItemPedido::setId));
	}
}
