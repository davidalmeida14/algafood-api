package com.algaworks.algafood.infraestructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algafood.domain.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
	
	public Optional<Estado> findByNome(String nome);
}
