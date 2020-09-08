package com.algaworks.algafood.api.controller;

import java.util.List;

import com.algaworks.algafood.api.model.input.PedidoInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.algaworks.algafood.api.assembler.impl.PedidoModelAssemblerImpl;
import com.algaworks.algafood.api.assembler.impl.PedidoResumoModelAssemblerImpl;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.CadastroPedidoService;

import javax.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private CadastroPedidoService pedidoService;
	
	@Autowired
	private PedidoModelAssemblerImpl pedidoAssembler;
	
	@Autowired
	private PedidoResumoModelAssemblerImpl pedidoResumoAssembler;
	
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

	@PostMapping
	public ResponseEntity<?> salvar (@RequestBody @Valid PedidoInput pedido) {
		return null;
	}

}
