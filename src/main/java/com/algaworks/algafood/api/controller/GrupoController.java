package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.impl.GrupoModelAssemblerImpl;
import com.algaworks.algafood.api.assembler.impl.GrupoModelDisassemblerImpl;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(value = "/grupos")
public class GrupoController {
	
	@Autowired
	private CadastroGrupoService cadastroGrupo;
	
	@Autowired
	private GrupoModelAssemblerImpl assembler;
	
	@Autowired
	private GrupoModelDisassemblerImpl disassembler;
	
	@PostMapping
	public ResponseEntity<GrupoModel> adicionar(@RequestBody @Valid GrupoInput grupoInput) {
		Grupo grupo = cadastroGrupo.cadastrar(disassembler.toDomainObject(grupoInput));
		return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(grupo));
	}
	
	@GetMapping
	public ResponseEntity<List<GrupoModel>> listar(){
		List<Grupo> grupos = cadastroGrupo.listar();
		return ResponseEntity.ok().body(assembler.toCollectionModel(grupos));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<GrupoModel> atualizar(@PathVariable("id") Long id, @RequestBody @Valid GrupoInput grupoInput){
		
		Grupo grupo = cadastroGrupo.buscar(id);
		disassembler.copyToDomainObject(grupoInput, grupo);
		
		grupo = cadastroGrupo.atualizar(grupo);
		
		GrupoModel modelResponse = assembler.toModel(grupo);
		
		return ResponseEntity.ok().body(modelResponse);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<GrupoModel> buscar(@PathVariable("id") Long id){
		Grupo grupoBuscado = cadastroGrupo.buscar(id);
		return ResponseEntity.ok().body(assembler.toModel(grupoBuscado));
	}

	@DeleteMapping("{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable("id") Long idGrupo) {
		cadastroGrupo.excluir(idGrupo);
	}
}
