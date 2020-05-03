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
import com.algaworks.algafood.domain.model.Cozinha;
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

}
