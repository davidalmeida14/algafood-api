package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CozinhaEmUsoException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.infraestructure.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService {

	
	@Autowired
	CozinhaRepository cozinhaRepository;
	
	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}
	
	@Transactional
	public void excluir(Long cozinhaId) {
		try {
			cozinhaRepository.deleteById(cozinhaId);	
			cozinhaRepository.flush();
			
		} catch(EmptyResultDataAccessException ex) {
			throw new CozinhaNaoEncontradaException(cozinhaId);
		} 
		catch(DataIntegrityViolationException ex) {
			throw new CozinhaEmUsoException(String.format(Constantes.MSG_COZINHA_EM_USO, cozinhaId));
		}
			
	}
	
	public Cozinha buscar(Long id) {
		
		return cozinhaRepository.findById(id)
								.orElseThrow(()-> new CozinhaNaoEncontradaException(id));
	}
}
