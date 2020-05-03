package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.algaworks.algafood.api.assembler.impl.CozinhaModelAssemblerImpl;
import com.algaworks.algafood.api.assembler.impl.CozinhaModelDisassemblerImpl;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.CozinhaXmlWrapper;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import com.algaworks.algafood.infraestructure.repository.CozinhaRepository;

@RestController
@RequestMapping(value = "/cozinhas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CozinhaModelAssemblerImpl cozinhaAssembler;
	
	@Autowired
	private CozinhaModelDisassemblerImpl cozinhaDisassembler;

	@GetMapping
	public ResponseEntity<List<CozinhaModel>> listar() {
		List<CozinhaModel> cozinhaResponse = cozinhaAssembler.toCollectionModel(cozinhaRepository.findAll());
		return ResponseEntity.ok().body(cozinhaResponse);
	}

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhaXmlWrapper listarXml() {
		return new CozinhaXmlWrapper(cozinhaRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CozinhaModel> buscar(@PathVariable("id") Long id) {
		CozinhaModel response = cozinhaAssembler.toModel(cadastroCozinha.buscar(id));
		return ResponseEntity.ok().body(response);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<CozinhaModel> adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = cozinhaDisassembler.toDomainObject(cozinhaInput);
		cozinha = cadastroCozinha.salvar(cozinha);
		CozinhaModel cozinhaResponse = cozinhaAssembler.toModel(cozinha);
		return ResponseEntity.status(HttpStatus.CREATED).body(cozinhaResponse);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<CozinhaModel> atualizar(@PathVariable Long id, @Valid @RequestBody CozinhaInput cozinhaInput) {

		Cozinha cozinhaAtual = cadastroCozinha.buscar(id);
		
		cozinhaDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		
		CozinhaModel cozinhaResponse = cozinhaAssembler.toModel(cadastroCozinha.salvar(cozinhaAtual));
		//BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");

		return ResponseEntity.ok().body(cozinhaResponse);

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		cadastroCozinha.excluir(id);
	}
}
