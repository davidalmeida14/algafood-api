package com.algaworks.algafood.infraestructure.repository;

import static com.algaworks.algafood.infraestructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infraestructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;

import lombok.var;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries  {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired @Lazy
	private RestauranteRepository restauranteRepository;
	
	@Override
	public List<Restaurante> find (String nome, 
			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
	
		log.info("Nome: " + nome);
		log.info("TaxaFreteInicial: " + taxaFreteInicial);
		log.info("TaxaFreteFinal: " + taxaFreteFinal);
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		
		Root<Restaurante> root = criteria.from(Restaurante.class);
		
		var listPredicates = new ArrayList<Predicate>();
		
		if(StringUtils.hasText(nome)) {
			listPredicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}
		
		if(taxaFreteInicial != null) {
			listPredicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
		}
		
		if(taxaFreteFinal != null) {
			listPredicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}

		criteria.where(listPredicates.toArray(new Predicate[0]));

		TypedQuery<Restaurante> typed = manager.createQuery(criteria);
		
		return typed.getResultList();
		
	}

	@Override
	public Integer countRestaurant() {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
		
		query.multiselect(builder.count(query.from(Restaurante.class)));
		
		return manager.createQuery(query).getSingleResult().intValue();
		
	}

	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
	}
	
	
}
