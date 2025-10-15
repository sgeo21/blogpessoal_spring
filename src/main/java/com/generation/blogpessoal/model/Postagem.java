package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// definir esta classe como model
@Entity //define como será a tabela na base de dados
@Table (name = "tb_postagens") //CREAT TABLE tb_postagens

public class Postagem {
	// não precisa fazer metodo contrutor, pois quem faz o método é o spring
		// criar getters and setters
		
		
	//definição de atributos da classe
	@Id //define a chave primária, PRIMARY KEY (id
	//atributos da tabela
	@GeneratedValue(strategy = GenerationType.IDENTITY) //define o autoincremento para executar no DB
	private Long id;
	
	//definições titulo
	@Column(length = 100) // para definir o tamanho do banco de dados, para que banco armazene tudo o que for incluso no titulo
	@NotBlank(message = "O atributo título é obrigatório!")//forçar a ter algo dentro de titulo
	@Size(min = 5, max =100, message = "O título deve conter no mínimo 5 e no máximo 100 caracteres") // definição de quantidade de caracteres
	private String titulo;
	
	//definir ações do texto
	@Column(length = 1000)// para definir o tamanho do banco de dados 
	@NotBlank(message = "O texto é obrigatório!")//forçar a ter algo dentro de texto
	@Size(min = 5, max =1000, message = "O texto deve conter no mínimo 5 e no máximo 100 caracteres") // definição de quantidade de caracteres
	private String texto;
	
	// definir questões da data
	@UpdateTimestamp // atualiza a data e hora da criação e atualização da postagem. CreatTimestamp seria se você não quiser nas atualizações mudar essa data
	private LocalDateTime data;
	
	//definir a junção das 
	@ManyToOne //define a ligação, muitas postagens para um tema
	@JsonIgnoreProperties("postagem") 
	private Tema tema;
	

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	public Tema getTema() {
		return tema;
	}
	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
	
}
