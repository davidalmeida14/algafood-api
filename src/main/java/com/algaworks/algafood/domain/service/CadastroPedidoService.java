package com.algaworks.algafood.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.model.enums.StatusPedido;
import com.algaworks.algafood.infraestructure.repository.PedidoRepository;

@Service
public class CadastroPedidoService {
	
	@Autowired
	private CadastroProdutoService cadastroProduto;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CadastroUsuarioService cadastroUsuario;
	
	@Autowired
	private FormaPagamentoService formaPagamentoService;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	public List<Pedido> listar(){
		List<Pedido> pedidos = pedidoRepository.findAll();
		return pedidos;
	}

	public Pedido buscar(Long pedidoId) {
		return pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}

	@Transactional
	public Pedido emitir(Pedido pedido) {
		validarPedido(pedido);
		validarItens(pedido);
		pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
		pedido.calcularValorTotal();
		return pedidoRepository.save(pedido);
	}


	private void validarItens(Pedido pedido) {
		pedido.getItens().forEach(item -> item.calcularPrecoTotal());
		pedido.getItens().forEach(item -> {
			item.getProduto().setRestaurante(pedido.getRestaurante());
			Produto produtoRestaurante = cadastroProduto.buscarProdutoRestaurante(item.getProduto(), pedido.getRestaurante());
			item.setPedido(pedido);
			item.setProduto(produtoRestaurante);
			item.setPrecoUnitario(produtoRestaurante.getPreco());
		});
		
	}


	private void validarPedido(Pedido pedido) {
		Cidade cidade = cadastroCidade.buscar(pedido.getEnderecoEntrega().getCidade().getId());
		Restaurante restaurante = cadastroRestaurante.buscar(pedido.getRestaurante().getId());
		Usuario usuario = cadastroUsuario.buscar(pedido.getCliente().getId());
		FormaPagamento formaPagamento = formaPagamentoService.buscar(pedido.getFormaPagamento().getId());
		
		pedido.setRestaurante(restaurante);
		pedido.getEnderecoEntrega().setCidade(cidade);
		pedido.setCliente(usuario);
		pedido.setFormaPagamento(formaPagamento);
		
		validarFormaPagamento(restaurante, formaPagamento);
	}


	private boolean validarFormaPagamento(Restaurante restaurante, FormaPagamento formaPagamento) {
		
		if(!restaurante.getFormasPagamentos().contains(formaPagamento)) {
			throw new NegocioException("Forma de pagamento %s não é aceita pelo restaurante");
		}
		return true;
	}

	/**
	 * Método que valida se pedido está elegível a confirmação
	 * @param pedidoBuscado
	 */
	private void validarStatusPedidoConfirmar(Pedido pedidoBuscado) {
		
		String mensagemErro = String.format("O pedido %d não pode ter seu status alterado para %s pois está com status: %s", pedidoBuscado.getId(), StatusPedido.CONFIRMADO.getMensagem(), pedidoBuscado.getStatusPedido().getMensagem());
		if(!StatusPedido.CRIADO.equals(pedidoBuscado.getStatusPedido()) && !StatusPedido.CONFIRMADO.equals(pedidoBuscado.getStatusPedido())) {
			throw new NegocioException(mensagemErro);
		}
		
	}
	
	private void validarStatusPedidoCancelar(Pedido pedidoBuscado) {
		
		String mensagemErro = String.format("O pedido %d não pode ter seu status alterado para %s pois está com status: %s", pedidoBuscado.getId(), StatusPedido.CANCELADO.getMensagem(), pedidoBuscado.getStatusPedido().getMensagem());
		if(!StatusPedido.CRIADO.equals(pedidoBuscado.getStatusPedido()) && !StatusPedido.CANCELADO.equals(pedidoBuscado.getStatusPedido())) {
			throw new NegocioException(mensagemErro);
		}
		
	}
	
	private void validarStatusPedidoEntregar(Pedido pedidoBuscado) {
		String mensagemErro = String.format("O pedido %d não pode ter seu status alterado para %s pois está com status: %s", pedidoBuscado.getId(), StatusPedido.ENTREGUE.getMensagem(), pedidoBuscado.getStatusPedido().getMensagem());
		if(!StatusPedido.CONFIRMADO.equals(pedidoBuscado.getStatusPedido()) && !StatusPedido.ENTREGUE.equals(pedidoBuscado.getStatusPedido())) {
			throw new NegocioException(mensagemErro);
		}
		
	}

	@Transactional
	public void confirmar(Long pedidoId) {
		Pedido pedidoBuscado = buscar(pedidoId);
		pedidoBuscado.confirmar();
	}
	
	@Transactional
	public void entregar(Long pedidoId) {
		Pedido pedidoBuscado = buscar(pedidoId);
		pedidoBuscado.entregar();
		
	}
	
	@Transactional
	public void cancelar(Long pedidoId) {
		Pedido pedidoBuscado = buscar(pedidoId);
		pedidoBuscado.cancelar();
		
	}
	
	public void validarStatusPedido(Pedido pedido, StatusPedido statusEsperado, StatusPedido statusAlterar) {
		
		String mensagemErro = String.format("O pedido %d não pode ter seu status alterado para %s pois está com status: %s", 
								pedido.getId(),
								statusAlterar.getMensagem(),
								pedido.getStatusPedido().getMensagem()
								);
		
		if(!pedido.getStatusPedido().equals(statusEsperado) && !pedido.getStatusPedido().equals(statusAlterar)) {
			throw new NegocioException(mensagemErro);
		}
	}
}
