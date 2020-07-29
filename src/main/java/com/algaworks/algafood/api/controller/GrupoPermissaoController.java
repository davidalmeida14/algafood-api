package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.impl.GrupoPermissaoAssembler;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.CadastroPermissaoService;

@RestController
@RequestMapping(value = "/grupos/{id}/permissoes")
public class GrupoPermissaoController {
	
	@Autowired
	private CadastroPermissaoService cadastroPermissaoService;
	
	@Autowired
	private GrupoPermissaoAssembler assembler;
	
	@GetMapping
	private ResponseEntity<?> listar(@PathVariable("id") Long idGrupo){
		Set<Permissao> listarPermissoes = cadastroPermissaoService.listarPermissoes(idGrupo);
		List<PermissaoModel> permissoes = assembler.toCollectionModel(listarPermissoes);
		return ResponseEntity.ok(permissoes);
	}
	
	@PutMapping(value = "/{idPermissao}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void associar(@PathVariable("id") Long idGrupo, @PathVariable("idPermissao") Long idPermissao){
		cadastroPermissaoService.atribuirPermissao(idGrupo, idPermissao);
	}
	
	@DeleteMapping(value = "/{idPermissao}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void desassociar(@PathVariable("id") Long idGrupo, @PathVariable("idPermissao") Long idPermissao){
		cadastroPermissaoService.atribuirPermissao(idGrupo, idPermissao);
	}

}
