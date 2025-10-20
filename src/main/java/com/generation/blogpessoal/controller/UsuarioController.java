package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

	
	@Autowired // Injeção de dependência do serviço de usuário
	private UsuarioService usuarioService;
	
	@GetMapping("/all") // Endpoint para buscar todos os usuários
	public ResponseEntity<List<Usuario>> getAll() {
		return ResponseEntity.ok(usuarioService.getAll());
	}
	@GetMapping("/{id}") // Endpoint para buscar usuário por ID
	public ResponseEntity<Usuario> getById(@PathVariable Long id) {
		return usuarioService.getById(id)
				.map(resposta-> ResponseEntity.ok(resposta)) // Retorna 200 se encontrado
				.orElse(ResponseEntity.notFound().build()); // Retorna 404 se não encontrado
	}
	
	@PostMapping("/cadastrar") // Endpoint para cadastrar um novo usuário
	public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {
		return usuarioService.cadastrarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta)) 
				.orElse(ResponseEntity.badRequest().build()); 
	}
	
	@PutMapping("/cadastrar") // Endpoint para editar um usuário
	public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {
		return usuarioService.atualizarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta)) 
				.orElse(ResponseEntity.notFound().build()); 
	}
	
	@PostMapping("/logar") // Endpoint para cadastrar um novo usuário
	public ResponseEntity<UsuarioLogin> autenticar(@Valid @RequestBody Optional <UsuarioLogin> usuarioLogin) {
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta)) 
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); 
	}
	
	
}
