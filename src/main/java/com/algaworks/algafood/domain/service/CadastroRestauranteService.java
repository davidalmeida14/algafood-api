package com.algaworks.algafood.domain.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.infraestructure.repository.CozinhaRepository;
import com.algaworks.algafood.infraestructure.repository.RestauranteRepository;

import br.com.twsoftware.alfred.object.Objeto;

@Service
public class CadastroRestauranteService {

	@Autowired
	RestauranteRepository restauranteRepository;

	@Autowired
	CozinhaRepository cozinhaRepository;

	@Autowired
	CadastroCozinhaService cozinhaService;
	
	@Autowired
	private CadastroCidadeService cidadeService;
	
	@Autowired
	private FormaPagamentoService formaPagamentoService;
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {

		if (Objeto.isBlank(restaurante.getCozinha())) {
			throw new NegocioException(String.format(Constantes.MSG_ID_NAO_INFORMADO));
		}
		Cozinha cozinha;
		try {
			cozinha = cozinhaService.buscar(restaurante.getCozinha().getId());
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
		restaurante.setCozinha(cozinha);

		return restauranteRepository.save(restaurante);
	}

	@Transactional
	public Restaurante atualizar(Long id, Restaurante restaurante) {

		Cozinha cozinhaBuscada;

		if (Objeto.isBlank(restaurante.getCozinha())) {
			throw new CozinhaNaoEncontradaException(Constantes.MSG_COZINHA_NAO_ENCONTRADA);
		}

		Restaurante restauranteBuscado = this.buscar(id);

		try {
			cozinhaBuscada = cozinhaService.buscar(restaurante.getCozinha().getId());
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
		
		BeanUtils.copyProperties(restaurante, restauranteBuscado, "id", "formaPagamento", "endereco", "dataCadastro");

		restauranteBuscado.setCozinha(cozinhaBuscada);

		return restauranteRepository.save(restauranteBuscado);
	}

	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restaurante = buscar(restauranteId);
		restaurante.ativar();
	}
	
	@Transactional
	public void inativar(Long restauranteId) {
		Restaurante restaurante = buscar(restauranteId);
		restaurante.inativar();
	}
	
	public Restaurante buscar(Long id) {
		return restauranteRepository.findById(id).orElseThrow(
				() -> new RestauranteNaoEncontradoException(id));
	}

	@Transactional
	public void excluir(Long id) {
		try {
			restauranteRepository.deleteById(id);
			restauranteRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(id);
		} catch (DataIntegrityViolationException e) {
			throw new RestauranteEmUsoException(id);
		}
	}
	
	@Transactional
	public Restaurante atualizarRestaurante(Restaurante restaurante) {
		
		Long cozinhaId = restaurante.getCozinha().getId();
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		
		Cozinha cozinha = cozinhaService.buscar(cozinhaId);
		Cidade cidade = cidadeService.buscar(cidadeId);
	
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		
		return restauranteRepository.save(restaurante);
	}
	
	@Transactional
	public void desassociarFormaPagamento(Long idRestaurante, Long idFormaPagamento) {
		
		Restaurante restaurante = buscar(idRestaurante);
		FormaPagamento formaPagamento = formaPagamentoService.buscar(idFormaPagamento);
		restaurante.removerFormaPagamento(formaPagamento);
		
	}
	
	@Transactional
	public void associarFormaPagamento(Long idRestaurante, Long idFormaPagamento) {
		Restaurante restaurante = buscar(idRestaurante);
		FormaPagamento formaPagamento = formaPagamentoService.buscar(idFormaPagamento);
		restaurante.adicionarFormaPagamento(formaPagamento);
	}
	
	@Transactional 
	public void abrir(Long id) {
		Restaurante restauranteBuscado = buscar(id);
		restauranteBuscado.abrir();
	}
	
	@Transactional
	public void fechar(Long id) {
		Restaurante restauranteBuscado = buscar(id);
		restauranteBuscado.fechar();
	}

}
