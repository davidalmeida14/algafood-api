package com.algaworks.algafood.domain.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@EqualsAndHashCode.Include
		private long id;
		
		@Column(nullable = false)
		private String nome;
		
		@Column(nullable = false)
		private String email;
		
		@Column(nullable = false)
		private String senha;
		
		@Column(nullable = false, columnDefinition = "datetime")
		@CreationTimestamp()
		private OffsetDateTime dataCadastro;
		
		@ManyToMany
		@JoinTable(name = "usuario_grupo",
				   joinColumns = @JoinColumn(name = "usuario_id"),
				   inverseJoinColumns = @JoinColumn(name = "grupo_id")) 
		private Set<Grupo> grupos = new HashSet<Grupo>();

		public boolean senhaNaoCoincideCom(String senhaAtualInformada) {
			return !this.senha.equalsIgnoreCase(senhaAtualInformada);
		}
		
		public void associarGrupo(Grupo grupo) {
			this.grupos.add(grupo);
		}
		
		public void desassociarGrupo(Grupo grupo) {
			this.grupos.remove(grupo);
		}
}
