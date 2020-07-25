package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradoException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.infraestructure.repository.FormaPagamentoRepository;

@Service
public class FormaPagamentoService {

	@Autowired
	private FormaPagamentoRepository repository;
	
	public List<FormaPagamento> listar(){
		return repository.findAll();
	}

	@Transactional
	public FormaPagamento cadastrar(FormaPagamento formaPagamento) {
		return repository.save(formaPagamento);
	}
	
	@Transactional
	public FormaPagamento atualizar(FormaPagamento formaPagamento) {
		return repository.saveAndFlush(formaPagamento);
	}

	public FormaPagamento buscar(Long idFormaPagamento) {
		return repository
					   .findById(idFormaPagamento)
					   .orElseThrow(() -> new FormaPagamentoNaoEncontradoException(idFormaPagamento));
		
	}
	@Transactional
	public void excluir(Long id) {
		try {
			repository.deleteById(id);
			repository.flush();
		} catch (EmptyResultDataAccessException ex) {
			throw new FormaPagamentoNaoEncontradoException(id);
		} 
		catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(Constantes.MSG_FORMA_PAGAMENTO_EM_USO, id));
		}
		
	}
	
}
