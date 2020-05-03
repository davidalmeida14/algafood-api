package com.algaworks.algafood.domain.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CidadeEmUsoException;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.infraestructure.repository.CidadeRepository;

import br.com.twsoftware.alfred.object.Objeto;

@Service
public class CadastroCidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroEstadoService estadoService;

	public List<Cidade> listar() {
		return cidadeRepository.findAll();
	}

	public Cidade buscar(Long id) {
		return cidadeRepository.findById(id)
				.orElseThrow(() -> new CidadeNaoEncontradaException(id));
	}

	@Transactional
	public Cidade salvar(Cidade cidade) {
		//validarEstadoCidade(cidade);
		Estado estado = estadoService.buscarOuFalhar(!Objects.isNull(cidade.getEstado().getId()) ? 
													  		 cidade.getEstado().getId() : 
													  	     estadoService.buscarPorNome(cidade.getEstado().getNome()).getId());
		cidade.setEstado(estado);
		return cidadeRepository.save(cidade);
	}

	@Transactional
	public void remover(Long id) {
		try {
			cidadeRepository.deleteById(id);
			cidadeRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(id);
		} catch (DataIntegrityViolationException e) {
			throw new CidadeEmUsoException(id);
		}
	}

	@Transactional
	public Cidade atualizar(Long id, Cidade cidade) {
		
		Cidade cidadeBuscada = buscar(id);
		validarEstadoCidade(cidade);
		Estado estadoRequest = cidade.getEstado();
		Estado estado = null;
		try {
			 estado = Objeto.isBlank(estadoRequest.getId()) ? estadoService.buscarPorNome(estadoRequest.getNome()) : estadoService.buscarOuFalhar(estadoRequest.getId());
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		cidade.setEstado(estado);
		BeanUtils.copyProperties(cidade, cidadeBuscada, "id");

		return cidadeRepository.save(cidadeBuscada);
	}

	private void validarEstadoCidade(Cidade cidade) {
		if(!Objeto.isBlank(cidade.getEstado())) {
			if(Objeto.isBlank(cidade.getEstado().getId())) {
				if(Objeto.isBlank(cidade.getEstado().getNome())) {
					throw new EstadoNaoEncontradoException(Constantes.MSG_ESTADO_NAO_ENCONTRADO);
				}
			}
		} 
	}


}
