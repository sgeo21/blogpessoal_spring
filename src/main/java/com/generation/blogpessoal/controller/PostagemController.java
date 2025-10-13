package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping ("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")// origins = siginifica que ele vai
// o allowedHeaders significa que aceita cabeçalhos, para aceitar apenas de uma pessoa 
//troca o "*" pelo "endereço do frontend"
//autorizar o beckend, receber requisições de outras origens 
public class PostagemController {

	//transferir a responsabilidade de criar a instancia do objeto, só indicar qual é o objeto
	//sendo assim o proprio spring cria o instanciamento do objeto 
	
	@Autowired // cria e diz que ele que vai ficar 'responsavel' por trazer os métodos de interação do banco.
	private PostagemRepository postagemRepository; // ingeção de dependencia, diz onde esses dados serão colocados
	
	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
		//SEleCT *FROM tb_postagens;
		
	}
}
