package com.generation.blogpessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long>{ 
	//tudo o que for utilizar postagem repository, vai usar o tb_postagem 
	// quem é a entidade e quem é i ID.
	// se tiver mais de um long ele daria erro, ai neste caso deveria ser usado Id.
	

	
	
}
