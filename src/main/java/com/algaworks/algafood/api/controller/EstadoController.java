package com.algaworks.algafood.api.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.assembler.impl.EstadoInputDisassemblerImpl;
import com.algaworks.algafood.api.assembler.impl.EstadoModelAssemblerImpl;
import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.CadastroEstadoService;
import com.algaworks.algafood.infraestructure.repository.EstadoRepository;

@RestController
@RequestMapping(value = "/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CadastroEstadoService estadoService;

	@Autowired
	private EstadoModelAssemblerImpl estadoAssembler;
	
	@Autowired
	private EstadoInputDisassemblerImpl estadoDisassembler;
	
	@GetMapping()
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}

	@PostMapping
	public ResponseEntity<EstadoModel> adicionar(@RequestBody @Valid EstadoInput estado) {
		
		Estado estadoSalvo = estadoService.adicionar(estadoDisassembler.toDomainObject(estado));
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(estadoSalvo.getId())
				.toUri();
		
		EstadoModel estadoResponse = estadoAssembler.toModel(estadoSalvo);
		return ResponseEntity.created(uri).body(estadoResponse);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EstadoModel> buscar(@PathVariable Long id) {
		EstadoModel estadoResponse = estadoAssembler.toModel(estadoService.buscarOuFalhar(id)); 
		return ResponseEntity.ok(estadoResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EstadoModel> atualizar(@PathVariable Long id, @Valid @RequestBody EstadoInput estado) {

		Estado estadoAtivo = estadoService.buscarOuFalhar(id);
			
		estadoDisassembler.copyToDomainObject(estado, estadoAtivo);

		estadoAtivo = estadoRepository.save(estadoAtivo);
		
		EstadoModel response = estadoAssembler.toModel(estadoAtivo);

		return ResponseEntity.ok(response);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
	
		estadoService.remover(id);
		
		return ResponseEntity.noContent().build();
	}

}
