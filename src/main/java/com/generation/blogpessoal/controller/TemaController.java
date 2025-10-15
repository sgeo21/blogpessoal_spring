package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

//para garantir que o front tenha acesso a essas informações
@RestController
@RequestMapping ("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

	//desenvolvimento da independencia do spring com o banco
	@Autowired 
	private TemaRepository temaRepository;
	
	//para encontrar temas 
	@GetMapping
	public ResponseEntity<List<Tema>> getAll(){
		return ResponseEntity.ok(temaRepository.findAll());
	}
	
	//para encontrar pelo id
	@GetMapping("/{id}") 
	public ResponseEntity<Tema> getById(@PathVariable Long id){ 
		return temaRepository.findById(id) 
         .map(resposta -> ResponseEntity.ok(resposta)) 
         .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); 
		
	}
	//pare encontrar pelo tema
	@GetMapping("/descricao/{descricao}")
	public ResponseEntity<List<Tema>> getAllByDescricao(@PathVariable String descricao){
		return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));
	}
	
	//Para postar o tema
	@PostMapping
	public ResponseEntity<Tema> post(@Valid @RequestBody Tema descricao){ // fiquei um pouco em dúvida aqui, pois será apenas um tema ou vários?
	descricao.setId(null); 
	return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(descricao));
	}
	
	// para atualizar o tema
	@PutMapping
	public ResponseEntity<Tema> put(@Valid @RequestBody Tema descricao){
		
	return temaRepository.findById(descricao.getId())
	          .map(resposta -> ResponseEntity.status(HttpStatus.OK)
	        		  .body(temaRepository.save(descricao)))
	          .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	//para deletar tema
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete (@PathVariable Long id) {
	   Optional<Tema> descricao = temaRepository.findById(id);
	   
	   if(descricao.isEmpty())
		   throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	   temaRepository.deleteById(id);
	   
	 
	}
	
}
