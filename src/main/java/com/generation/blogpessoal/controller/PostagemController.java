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

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;

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
	@GetMapping("/{id}") // get mapping é uma saída quando procura pelo id
	public ResponseEntity<Postagem> getById(@PathVariable Long id){ // indica que id virá da url
		return postagemRepository.findById(id) //busca a postagem por id
         .map(resposta -> ResponseEntity.ok(resposta)) //se encontrar responde com ok
         .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // responde que não foi encontrado
		        //.orElse(ResponseEntity.notFound().build()); // pode ser o anterior ou esse.
		//SELECT * FROM tb_postagem Where id = id;
	}
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		
      }
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
	postagem.setId(null); //para garantir que a postagem será nova
	return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
	}
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		
	return postagemRepository.findById(postagem.getId())
	          .map(resposta -> ResponseEntity.status(HttpStatus.OK)
	        		  .body(postagemRepository.save(postagem)))
	          .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	         	  
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete (@PathVariable Long id) {
	   Optional<Postagem> postagem = postagemRepository.findById(id);
	   
	   if(postagem.isEmpty())
		   throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	   postagemRepository.deleteById(id);
	   
	 
	}
}


