package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.algaworks.algafood.api.assembler.impl.ProdutoAssemblerImpl;
import com.algaworks.algafood.api.assembler.impl.ProdutoDisassemblerImpl;
import com.algaworks.algafood.api.model.ProdutosRestauranteModel;
import com.algaworks.algafood.api.model.input.ProdutoRestauranteInput;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.algaworks.algafood.infraestructure.repository.ProdutoRepository;

@RestController
@RequestMapping("/restaurantes/{id}/produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoAssemblerImpl produtoAssembler;
	
	@Autowired
	private ProdutoDisassemblerImpl disassembler;
	
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	
	@GetMapping
	@Cacheable(value = "produto")
	public List<ProdutosRestauranteModel> listar(@PathVariable("id") Long id, @RequestParam(required = false) boolean incluirTodosProdutos) {
		Restaurante restaurante = restauranteService.buscar(id);
		List<Produto> todosProdutos = null;
		if(incluirTodosProdutos) {
			todosProdutos = produtoRepository.findByRestaurante(restaurante);
		} else {
			todosProdutos = produtoRepository.findByRestauranteAndAtivo(restaurante, true);
		}
		return produtoAssembler.toCollectionModel(todosProdutos);
	}
	
	@Cacheable(value = "produto")
	@GetMapping("/{idProduto}")
	public ResponseEntity<ProdutosRestauranteModel> buscarProdutoRestaurante(@PathVariable("idProduto") Produto produto, @PathVariable("id") Restaurante restaurante) {
		Produto produtoBuscado = cadastroProdutoService.buscarProdutoRestaurante(produto, restaurante);
		return ResponseEntity.ok().body(produtoAssembler.toModel(produtoBuscado));
	}

	@PostMapping
	@CacheEvict(value = "produto", allEntries = true)
	public ResponseEntity<?> cadastrar(@PathVariable("id") Long restauranteId, @RequestBody @Valid ProdutoRestauranteInput produtoInput){
		Produto produto = disassembler.toDomainObject(produtoInput);
		Produto produtoCadastrado = cadastroProdutoService.cadastrar(restauranteId, produto);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoAssembler.toModel(produtoCadastrado));
	}

	@PutMapping("/{idProduto}")
	@CacheEvict(value = "produto")
	public ResponseEntity<?> atualizarProduto(@PathVariable("id") Long restauranteId, 
											  @PathVariable("idProduto") Long produtoId,
											  @RequestBody @Valid ProdutoRestauranteInput input){
		Produto produtoAtualizado = cadastroProdutoService.atualizarProduto(restauranteId, produtoId, input);
		return ResponseEntity.ok(produtoAssembler.toModel(produtoAtualizado));
	}
	
}
