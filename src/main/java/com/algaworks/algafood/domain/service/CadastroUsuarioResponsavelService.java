package com.algaworks.algafood.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.assembler.impl.UsuarioAssemblerImpl;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.infraestructure.repository.RestauranteRepository;

@Service
public class CadastroUsuarioResponsavelService {

	
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@Autowired
	private CadastroUsuarioService usuarioService;
	
	@Autowired
	private UsuarioAssemblerImpl usuarioAssembler;
	
	@Transactional
	public void associarUsuarioRestaurante(Long usuarioId, Long restauranteId) {
		Usuario usuarioBuscado = usuarioService.buscar(usuarioId);
		Restaurante restauranteBuscado = restauranteService.buscar(restauranteId);
		restauranteBuscado.associarUsuario(usuarioBuscado);
	}
	
	@Transactional
	public void desassociarUsuarioRestaurante(Long usuarioId, Long restauranteId) {
		Usuario usuarioBuscado = usuarioService.buscar(usuarioId);
		Restaurante restauranteBuscado = restauranteService.buscar(restauranteId);
		restauranteBuscado.desassociarUsuario(usuarioBuscado);
	}
	
	public List<UsuarioModel> listarResponsaveis(Long restauranteId){
		Restaurante restaurante = restauranteService.buscar(restauranteId);
		List<UsuarioModel> usuarios = restaurante.getUsuarios().stream().map(u -> usuarioAssembler.toModel(u)).collect(Collectors.toList());
		return usuarios;
	}

}
