package com.algaworks.algafood.domain.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.infraestructure.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CadastroGrupoService grupoService;
	
	@Autowired
	private MessageSource messageSource;
	
	private static String KEYMESSAGENOTFOUND = "usuario-nao-encontrado";
	
	@Transactional
	public Usuario cadastrar(Usuario usuario) {
		//Evita UPDATE de sincronização antecipada devido o @Transactional
		usuarioRepository.detach(usuario);
		Optional<Usuario> usuarioBuscado = usuarioRepository.findByEmail(usuario.getEmail());
		if(usuarioBuscado.isPresent() && usuarioBuscado.get().equals(usuario)) {
			throw new NegocioException(String.format(Constantes.MSG_USUARIO_EXISTENTE, usuario.getEmail()));
		}
		return usuarioRepository.save(usuario);
	}

	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}

	public Usuario buscar(Long idUsuario) {
		return usuarioRepository.findById(idUsuario)
								.orElseThrow(() -> new UsuarioNaoEncontradoException(recuperarMensagemErroPorId(idUsuario, KEYMESSAGENOTFOUND)));
	}
	
	@Transactional
	public Usuario atualizar(Usuario usuarioBuscado) {
		return usuarioRepository.saveAndFlush(usuarioBuscado);
	}

	@Transactional
	public void alterarSenha(Long id, SenhaInput senhaInput) {
		
		Usuario usuario = buscar(id);
		
		validaSenhaAtual(usuario, senhaInput);
		
		usuario.setSenha(senhaInput.getNovaSenha());
		
		usuarioRepository.save(usuario);
		
	}

	private void validaSenhaAtual(Usuario usuario, SenhaInput senhaInput) {
		if(usuario.senhaNaoCoincideCom(senhaInput.getSenhaAtual())){
			throw new NegocioException("Senha atual não coincide com a senha do usuário");
		}
	}
	
	private String recuperarMensagemErroPorId(Long id, String keyMessage) {
		return messageSource.getMessage(keyMessage, new Object[] {id.toString()}, LocaleContextHolder.getLocale());
	}

	@Transactional
	public void desassociarGrupo(Long idUsuario, Long idGrupo) {
		Usuario usuarioBuscado = buscar(idUsuario);
		Grupo grupoBuscado = grupoService.buscar(idGrupo);
		usuarioBuscado.desassociarGrupo(grupoBuscado);
	}
	
	@Transactional
	public void associarGrupo(Long idUsuario, Long idGrupo) {
		Usuario usuarioBuscado = buscar(idUsuario);
		Grupo grupoBuscado = grupoService.buscar(idGrupo);
		usuarioBuscado.associarGrupo(grupoBuscado);
	}

}
