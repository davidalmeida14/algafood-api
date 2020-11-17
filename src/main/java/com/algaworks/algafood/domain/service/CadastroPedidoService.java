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

	public Pedido buscar(String codigo) {
		return pedidoRepository.findByCodigo(codigo).orElseThrow(() -> new PedidoNaoEncontradoException(codigo));
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

	@Transactional
	public void confirmar(String codigo) {
		Pedido pedidoBuscado = buscar(codigo);
		pedidoBuscado.confirmar();
	}
	
	@Transactional
	public void entregar(String codigo) {
		Pedido pedidoBuscado = buscar(codigo);
		pedidoBuscado.entregar();
		
	}
	
	@Transactional
	public void cancelar(String codigo) {
		Pedido pedidoBuscado = buscar(codigo);
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
