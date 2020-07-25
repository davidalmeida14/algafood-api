package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.assembler.impl.UsuarioAssemblerImpl;
import com.algaworks.algafood.api.assembler.impl.UsuarioDisassemblerImpl;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.api.model.input.UsuarioSemSenhaInput;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioAssemblerImpl assembler;
	
	@Autowired 
	private UsuarioDisassemblerImpl disassembler;
	
	@Autowired
	private CadastroUsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<UsuarioModel> criar(@RequestBody @Valid UsuarioInput usuarioInput){
		Usuario usuario = disassembler.toDomainObject(usuarioInput);
		usuario = usuarioService.cadastrar(usuario);
		UsuarioModel response = assembler.toModel(usuario);
		return ResponseEntity.created(ServletUriComponentsBuilder
									  	.fromCurrentRequestUri()
									  	.buildAndExpand("{id}", response.getId()).toUri())
							 .body(response);
		
	}
	@GetMapping
	public ResponseEntity<List<UsuarioModel>> listar(){
		List<UsuarioModel> response = assembler.toCollectionModel(usuarioService.listar());
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioModel> buscar(@PathVariable("id") Long idUsuario){
		Usuario usuario = usuarioService.buscar(idUsuario);
		UsuarioModel response = assembler.toModel(usuario);
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioModel> atualizar(@PathVariable("id") Long id, @RequestBody @Valid UsuarioSemSenhaInput input){
		
		Usuario usuarioBuscado = usuarioService.buscar(id);
		
		disassembler.copyToDomainObject(input, usuarioBuscado);
		
		usuarioBuscado = usuarioService.atualizar(usuarioBuscado);
		
		return ResponseEntity.ok(assembler.toModel(usuarioBuscado));
	}
	
	@PatchMapping("/{id}/senha")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void alterarSenha(@PathVariable Long id, @RequestBody @Valid SenhaInput senhaInput){
		usuarioService.alterarSenha(id, senhaInput);
	}

}
