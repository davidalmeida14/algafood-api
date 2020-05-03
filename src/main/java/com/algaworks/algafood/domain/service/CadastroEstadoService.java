package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.infraestructure.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

	@Autowired
	EstadoRepository estadoRepository;

	@Transactional
	public Estado atualizar(Estado estado) {
		return estadoRepository.save(estado);
	}

	@Transactional
	public void remover(Long id) {

		try {
			estadoRepository.deleteById(id);
			estadoRepository.flush();
		} catch (EmptyResultDataAccessException ex) {
			throw new EstadoNaoEncontradoException(id);

		} catch (DataIntegrityViolationException ex) {
			throw new EntidadeEmUsoException(String.format("Estado %s não pode ser excluído pois está em uso", id));
		}

	}

	@Transactional
	public Estado adicionar(Estado estado) {
		Optional<Estado> estadoResult = estadoRepository.findByNome(estado.getNome());
		if(estadoResult.isPresent()) {
			throw new EntidadeEmUsoException("Estado já está cadastrado");
		}
		return estadoRepository.save(estado);
	}

	public Estado buscarOuFalhar(Long id) {
		return estadoRepository.findById(id)
				.orElseThrow(() -> new EstadoNaoEncontradoException(id));
	}

	public Estado buscarPorNome(String nome) {
		return estadoRepository.findByNome(nome).orElseThrow(()-> new EstadoNaoEncontradoException(String.format(Constantes.MSG_ESTADO_NAO_ENCONTRADO, nome)));
	}

}
