package com.algaworks.algafood.infraestructure.repository.spec;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.infraestructure.repository.filter.PedidoFilter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PedidoSpecs {

	private static final String PERCENT = "%";
	
	public static Specification<Pedido> usandoFiltro(PedidoFilter filtro){
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();

			root.fetch("restaurante").fetch("cozinha");
			root.fetch("cliente");
			root.fetch("formaPagamento");

			if(Objects.nonNull(filtro.getClienteId())){
				predicates.add(builder.equal(root.get("cliente"), filtro.getClienteId()));
			}

			if(Objects.nonNull(filtro.getRestauranteId())){
				predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
			}

			if(Objects.nonNull(filtro.getDataCriacaoInicio())){
				predicates.add(builder.greaterThan(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
			}

			if(Objects.nonNull(filtro.getDataCriacaoFim())){
				predicates.add(builder.lessThan(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
}
