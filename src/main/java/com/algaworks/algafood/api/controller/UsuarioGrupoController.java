package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.impl.GrupoModelAssemblerImpl;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(value = "/usuarios/{id}/grupos")
public class UsuarioGrupoController {
	
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Autowired
	private GrupoModelAssemblerImpl assembler;
	
	@GetMapping
	public ResponseEntity<List<GrupoModel>> listar(@PathVariable("id") Long idUsuario) {
		Usuario usuarioBuscado = cadastroUsuarioService.buscar(idUsuario);
		List<GrupoModel> grupos = usuarioBuscado.getGrupos().stream().map(g -> assembler.toModel(g)).collect(Collectors.toList());
		return ResponseEntity.ok(grupos);
	}
	
	@DeleteMapping(value = "/{idGrupo}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void desassociarGrupo(@PathVariable("id") Long idUsuario, @PathVariable("idGrupo") Long idGrupo){
		cadastroUsuarioService.desassociarGrupo(idUsuario, idGrupo);
	}
	
	@PutMapping("/{idGrupo}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void associarGrupo(@PathVariable("id") Long idUsuario, @PathVariable("idGrupo") Long idGrupo) {
		cadastroUsuarioService.associarGrupo(idUsuario, idGrupo);
	}

}
