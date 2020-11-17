package com.algaworks.algafood.domain.service;

import java.util.Objects;

import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.api.assembler.impl.ProdutoDisassemblerImpl;
import com.algaworks.algafood.api.model.input.ProdutoRestauranteInput;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.infraestructure.repository.ProdutoRepository;

@Service
@Slf4j
public class CadastroProdutoService {
	
	
	private static final String PRODUTO_NAO_ENCONTRADO = "O produto informado não foi encontrado";
	private static final String PRODUTO_NAO_PERTENCE_RESTAURANTE= "O produto %s não pertence ao restaurante %s";
	private static final String RESTAURANTE_NAO_ENCONTRADO = "O restaurante informado não foi encontrado";
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoDisassemblerImpl produtoDisassembler;
	
	@Autowired
	private CadastroRestauranteService restauranteService;

	public CadastroProdutoService(){
		log.info("Carregando a classe: {}", this.getClass().getSimpleName());
	}

	public Produto buscarProdutoRestaurante(Produto produto, Restaurante restaurante) {
		validarDadosRecebidos(produto, restaurante);
		validaSeProdutoPertenceARestaurante(produto, restaurante);
		Produto produboBuscado = produtoRepository.findById(produto.getId()).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado"));
		return produboBuscado;
		
	}

	private void validaSeProdutoPertenceARestaurante(Produto produto, Restaurante restaurante) {
		Long idRestauranteProduto = produto.getRestaurante().getId();
		Long idRestauranteUrl = restaurante.getId();
		if(idRestauranteProduto.compareTo(idRestauranteUrl) != 0) {
			throw new NegocioException(String.format(PRODUTO_NAO_PERTENCE_RESTAURANTE, produto.getId(), idRestauranteUrl));
		}
	}

	private void validarDadosRecebidos(Produto produto, Restaurante restaurante) {
		
		if(Objects.isNull(produto)) {
			throw new ProdutoNaoEncontradoException(PRODUTO_NAO_ENCONTRADO);
		}
		
		if(Objects.isNull(restaurante)) {
			throw new RestauranteNaoEncontradoException(RESTAURANTE_NAO_ENCONTRADO);
		}
		
	}

	@Transactional
	public Produto atualizarProduto(Long restauranteId, Long produtoId, ProdutoRestauranteInput input) {
		Produto produtoBuscado = buscar(produtoId);
		Restaurante restauranteBuscado = restauranteService.buscar(restauranteId);
		Produto produtoDoRestaurante = buscarProdutoRestaurante(produtoBuscado, restauranteBuscado);
		produtoDisassembler.copyToDomainObject(input, produtoDoRestaurante);
		return salvar(produtoDoRestaurante);
		
	}
	@Transactional
	private Produto salvar(Produto produtoDoRestaurante) {
		return produtoRepository.save(produtoDoRestaurante);
	}

	private Produto buscar(Long produtoId) {
		return produtoRepository.findById(produtoId)
								.orElseThrow(() -> new ProdutoNaoEncontradoException(PRODUTO_NAO_ENCONTRADO));
	}

	@Transactional
	public Produto cadastrar(Long restauranteId, Produto produto) {
		Restaurante restaurante = restauranteService.buscar(restauranteId);
		produto.setRestaurante(restaurante);
		return produtoRepository.save(produto);
	}
}
