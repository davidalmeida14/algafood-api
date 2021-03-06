package com.algaworks.algafood.infraestructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface PedidoRepository extends CustomJpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {
	
	List<Pedido> findByRestaurante(Restaurante restaurante);

	Optional<Pedido> findByCodigo(String codigo);

	@Query("from Pedido p join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha join p.formaPagamento")
	public List<Pedido> findAll();

}
