package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.infraestructure.repository.PedidoRepository;

@Service
public class CadastroPedidoService {
	
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	public List<Pedido> listar(){
		List<Pedido> pedidos = pedidoRepository.findAll();
		return pedidos;
	}

	
	public Pedido buscar(Long pedidoId) {
		return pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}
}
