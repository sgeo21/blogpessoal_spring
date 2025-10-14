package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
	// tudo o que for utilizar postagem repository, vai usar o tb_postagem
	// quem é a entidade e quem é i ID.
	// se tiver mais de um long ele daria erro, ai neste caso deveria ser usado Id.

	public List<Postagem> findAllByTituloContainingIgnoreCase(String titulo);

    //SELECT*FROM tb_postagens WHERE titulo LIKE "%?%";
	// find = Seletc, All = *, By = WHERE, Titulo = titulo, Containing = like, 
	//Ignore case = não diferenciar maiúsculo de minúsculo.

	
}
