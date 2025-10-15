package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_temas") // criar tabela temas
public class Tema {

	// Autoincremento para id
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Definições do atributo tema com restrição de tamanho
	@Column(length = 500)
	@NotBlank(message = "Incluir uma descrição é obrigatório!")
	@Size(min = 5, max =500, message = "A descrição deve conter no mínimo 5 e no máximo 500 caracteres") 
	private String descricao;
	
	//definição da comunicação das duas classes models
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE)  
	// Lazy = carrega os dados quando for chamado e não quando chamar a classe tema,
	// forma definida de comunicação entre a model Tema e a model Postagem
	@JsonIgnoreProperties(value = "tema", allowSetters = true) // evitar loopings infinitos, mas não ignora os setts
     private List<Postagem> postagem; 
	
	//método getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
	
	
}
