package com.algaworks.algafood.infraestructure.repository;

import java.math.BigDecimal;
import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;

public interface RestauranteRepositoryQueries {

	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

	Integer countRestaurant();
	List<Restaurante> findComFreteGratis(String nome);
}