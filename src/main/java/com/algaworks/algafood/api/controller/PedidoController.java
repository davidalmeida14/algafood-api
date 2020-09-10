package com.algaworks.algafood.api.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.impl.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.impl.PedidoModelAssemblerImpl;
import com.algaworks.algafood.api.assembler.impl.PedidoResumoModelAssemblerImpl;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.CidadeIdInput;
import com.algaworks.algafood.api.model.input.EnderecoInput;
import com.algaworks.algafood.api.model.input.FormaPagamentoIdInput;
import com.algaworks.algafood.api.model.input.ItemPedidoInput;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.api.model.input.RestauranteIdInput;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroPedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private CadastroPedidoService pedidoService;
	
	@Autowired
	private PedidoModelAssemblerImpl pedidoAssembler;
	
	@Autowired
	private PedidoResumoModelAssemblerImpl pedidoResumoAssembler;
	
	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;
	
	@GetMapping
	public ResponseEntity<?> listarPedidos() {
		List<Pedido> pedidos = pedidoService.listar();
		List<PedidoResumoModel> collectionModel = pedidoResumoAssembler.toCollectionModel(pedidos);
		return ResponseEntity.ok(collectionModel);
	}
	
	@GetMapping(value = "/{pedidoId}")
	public ResponseEntity<?> buscar(@PathVariable Long pedidoId) {
		Pedido buscar = pedidoService.buscar(pedidoId);
		PedidoModel model = pedidoAssembler.toModel(buscar);
		return ResponseEntity.ok(model);
	}

	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping
	public ResponseEntity<?> salvar (@RequestBody @Valid PedidoInput pedidoInput) {
		Pedido pedido = pedidoInputDisassembler.toDomainObject(pedidoInput);
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		pedido.setCliente(usuario);
		Pedido pedidoEmitido = pedidoService.emitir(pedido);
		return ResponseEntity.ok().body(pedidoAssembler.toModel(pedidoEmitido));
	}

	
	public static void main(String[] args) throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		PedidoInput input = new PedidoInput();
		
		RestauranteIdInput restauranteIdInput = new RestauranteIdInput();
		restauranteIdInput.setId(1L);
		
		EnderecoInput enderecoInput = new EnderecoInput();
		enderecoInput.setBairro("Costa e Silva");
		enderecoInput.setCidade(new CidadeIdInput(1L));
		enderecoInput.setCep("38400-000");
		enderecoInput.setNumero("1242");
		enderecoInput.setLogradouro("Rua Floriano Peixoto");
		
		ItemPedidoInput itemPedido1 = new ItemPedidoInput();
		itemPedido1.setProdutoId(1L);
		itemPedido1.setObservacao("Sem alface");
		itemPedido1.setQuantidade(2);
		
		ItemPedidoInput itemPedido2 = new ItemPedidoInput();
		itemPedido2.setProdutoId(2L);
		itemPedido2.setObservacao("Sem molho");
		itemPedido2.setQuantidade(3);
		
		List<ItemPedidoInput> listaItens = Arrays.asList(itemPedido1, itemPedido2);
		
		FormaPagamentoIdInput formaPagamentoInput = new FormaPagamentoIdInput();
		formaPagamentoInput.setId(1L);
		
		input.setRestaurante(restauranteIdInput);
		input.setEnderecoEntrega(enderecoInput);
		input.setFormaPagamento(formaPagamentoInput);
		input.setItens(listaItens);
		
		json = mapper.writeValueAsString(input);
		System.out.println(json);
	}
}
