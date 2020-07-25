package com.algaworks.algafood.api.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.algaworks.algafood.api.assembler.impl.FormaPagamentoAssemblerImpl;
import com.algaworks.algafood.api.assembler.impl.FormaPagamentoDisassemblerImpl;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.service.FormaPagamentoService;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {
	
	
	@Autowired
	private FormaPagamentoService formaPagamentoService;
	
	@Autowired
	private FormaPagamentoAssemblerImpl assembler;
	
	@Autowired
	private FormaPagamentoDisassemblerImpl disassembler;
	
	@GetMapping
	public ResponseEntity<List<FormaPagamentoModel>> listar() {
		List<FormaPagamentoModel> formaPagamentoResponse = assembler.toCollectionModel(formaPagamentoService.listar());
		return ResponseEntity.ok(formaPagamentoResponse);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable("id") Long id){
		FormaPagamentoModel formaPagamentoResponse = assembler.toModel(formaPagamentoService.buscar(id));
		return ResponseEntity.ok().body(formaPagamentoResponse);
	}
	
	@PostMapping
	public ResponseEntity<FormaPagamentoModel> cadastrar(@RequestBody FormaPagamentoInput input){
		FormaPagamento formaPagamentoCadastrado = formaPagamentoService.cadastrar(disassembler.toDomainObject(input));
		URI uri = recuperarURI(formaPagamentoCadastrado);
		FormaPagamentoModel formaPagamentoResponse = assembler.toModel(formaPagamentoCadastrado);
		return ResponseEntity.created(uri).body(formaPagamentoResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		formaPagamentoService.excluir(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<FormaPagamentoModel> atualizar(@PathVariable("id") Long id, @Valid @RequestBody FormaPagamentoInput update) {
		
		FormaPagamento buscado = formaPagamentoService.buscar(id);
		
		disassembler.copyToDomainObject(update, buscado);
		
		buscado = formaPagamentoService.atualizar(buscado);
		
		FormaPagamentoModel response = assembler.toModel(buscado);
		
		return ResponseEntity.ok().body(response);
	}
	private URI recuperarURI(FormaPagamento formaPagamentoCadastrado) {
		return ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand("{id}", formaPagamentoCadastrado.getId()).toUri();
	}

}
