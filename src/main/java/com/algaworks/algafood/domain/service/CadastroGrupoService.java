package com.algaworks.algafood.domain.service;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.GrupoExistenteException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.infraestructure.repository.GrupoRepository;

@Service
public class CadastroGrupoService {
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Transactional
	public Grupo cadastrar(Grupo grupo) {
		Grupo grupoBase = grupoRepository.findByNome(grupo.getNome());
		if(Objects.isNull(grupoBase)) {
			return grupoRepository.save(grupo);
		} else {
			throw new GrupoExistenteException(String.format(Constantes.MSG_GRUPO_USO_EXCEPTION, grupo.getNome()));
		}
	}

	public List<Grupo> listar() {
		return grupoRepository.findAll();
	}

	public Grupo buscar(Long id) {
		return grupoRepository.findById(id)
				.orElseThrow(()-> new GrupoNaoEncontradoException(recuperarMensagemErroPorId(id, Constantes.KEY_GRUPO_NAO_ENCONTRADO)));
	}

	@Transactional
	public Grupo atualizar(Grupo grupo) {
		return grupoRepository.saveAndFlush(grupo);
	}
	
	private String recuperarMensagemErroPorId(Long id, String keyMessage) {
		return messageSource.getMessage(keyMessage, new Object[] {id}, LocaleContextHolder.getLocale());
	}

	@Transactional
	public void excluir(Long idGrupo) {
		try {
			grupoRepository.deleteById(idGrupo);
		} catch (EmptyResultDataAccessException e) {
			throw new GrupoNaoEncontradoException(recuperarMensagemErroPorId(idGrupo, Constantes.KEY_GRUPO_NAO_ENCONTRADO));
		}
		
		
	}

}
