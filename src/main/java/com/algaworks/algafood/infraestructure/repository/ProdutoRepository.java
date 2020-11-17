package com.algaworks.algafood.infraestructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface ProdutoRepository extends CustomJpaRepository<Produto, Long>{

	List<Produto> findByRestaurante(Restaurante restaurante);
	List<Produto> findByRestauranteAndAtivo(Restaurante restaurante, boolean ativo);

}
