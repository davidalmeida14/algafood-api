package com.algaworks.algafood.domain.service;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.infraestructure.repository.PermissaoRepository;

@Service
public class CadastroPermissaoService {

	
	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Transactional
	public void atribuirPermissao(Long idGrupo, Long idPermissao) {
		Grupo grupoBuscado = cadastroGrupoService.buscar(idGrupo);
		Permissao permissaoBuscada = buscar(idPermissao);	
		grupoBuscado.associarPermissao(permissaoBuscada);
	}
	
	@Transactional
	public void desassociarPermissao(Long idGrupo, Long idPermissao) {
		Grupo grupoBuscado = cadastroGrupoService.buscar(idGrupo);
		Permissao permissaoBuscada = buscar(idPermissao);	
		grupoBuscado.desassociarPermissao(permissaoBuscada);
	}

	private Permissao buscar(Long idPermissao) {
		return permissaoRepository.findById(idPermissao).orElseThrow(() -> new PermissaoNaoEncontradaException("A permissão %s não foi encontrada"));
	}

	public Set<Permissao> listarPermissoes(Long idGrupo) {
		Grupo grupoBuscado = cadastroGrupoService.buscar(idGrupo);
		Set<Permissao> permissoes = grupoBuscado.getPermissoes();
		return permissoes;
	}

}
