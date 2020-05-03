package com.algaworks.algafood.infraestructure.repository.spec;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.model.Restaurante;

public class RestauranteSpecs {

	private static final String PERCENT = "%";
	
	public static Specification<Restaurante> comFreteGratis(){
		return (root, query, builder) -> {
			return builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
		};
	}
	
	public static Specification<Restaurante> comNomeSemelhante(String nome) {
		return (root, query, builder) -> {
			return builder.like(root.get("nome"), PERCENT.concat(nome).concat(PERCENT));
		};
	}
}
