package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import com.generation.blogpessoal.util.JwtHelper;
import com.generation.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // indica que é uma classe de teste,
																			// WebEnvironment.RANDOM_PORT: pegar porta
																			// aleatoria
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class) // DisplayName: nome do teste
public class UsuarioControllerTest {

	// Injeções de dependencia:
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository; // limpar os dados da tabela quando finalizar os testes

	// Criar as constantes:
	private static final String BASE_URL = "/usuarios"; // o endpoint
	private static final String USUARIO = "root@root.com"; // indica o usuario, para fazer a autenticacao
	private static final String SENHA = "rootroot"; // indica a senha

	// Metodo para iniciar antes dos teste
	@BeforeAll
	void inicio() {
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Root", USUARIO, SENHA)); // null: id do usuario
																									// , Root: seria o
																									// usuario Root,
	}

	// Metodos para criar os testes

	@Test
	@DisplayName("01 - Deve cadastrar um novo usuário com sucesso")
	void deveCadastrarUsuario() {
		// Modelo de testes: Given - When - Then
		// Given: define o cenario, o que vai testar, pegar esse usuario e cadastrar no
		// api
		Usuario usuario = TestBuilder.criarUsuario(null, "Thuany", "thuany@email.com.br", "12345678");

		// When: é a ação principal
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				requisicao, Usuario.class);

		// Then: checar, verifica se a resposta que veio é aquilo que espera
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());// verifica se a resposta foi CREATED
		assertNotNull(resposta.getBody());// vrifica se veio algo no corpo da requisição
	}

	@Test
	@DisplayName("2 - Não deve cadastrar usuário duplicado")
	void naoDeveCadastrarUsuarioDuplicado() {
		// Given - preparando o cenário de teste (o corpo da requisição)
		Usuario usuario = TestBuilder.criarUsuario(null, "Rafaela Lemes", "rafaelalemes@email.com.br", "12345678");
		usuarioService.cadastrarUsuario(usuario);

		// When - enviando a requisição
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				requisicao, Usuario.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		assertNull(resposta.getBody());

	}

	@Test
	@DisplayName("3 - Deve Atualizar os dados do usuário com sucesso")
	void DeveAtualizarUmUsuario() {
		// Given - preparando o cenário de teste (o corpo da requisição)
		Usuario usuario = TestBuilder.criarUsuario(null, "Nadia", "nadia@email.com.br", "12345678");
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);

		Usuario usuarioUpdate = TestBuilder.criarUsuario(usuarioCadastrado.get().getId(), "Nadia Caricatto", "nadia@email.com.br", "abc12345");

		// When - enviando a requisição
		// Gerar o token:
		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);

		// Criar usuario:
		HttpEntity<Usuario> requisicao = JwtHelper.criarRequisicaoComToken(usuarioUpdate, token);

		// Enviar a requisição PUT
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL + "/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());

	}
	
	@Test
	@DisplayName("✔ 04 - Deve listar todos os usuários com sucesso")
	void deveListarTodosUsuarios() {
		
		// Given
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Juliana", 
				"Juna_marques@email.com.br", "qwe45678"));
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Vinicius", 
				"vini_af@email.com.br", "12345abc"));
		
		// When
		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		ResponseEntity<Usuario[]> resposta = testRestTemplate.exchange(
				BASE_URL + "/all", HttpMethod.GET, requisicao, Usuario[].class);
		
		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("✔ 05 - Deve listar usuarios por id")
	void deveListarporId() {
			
		// Given
			Usuario usuario = usuarioService.cadastrarUsuario(
				    TestBuilder.criarUsuario(null, "Nome", "email@email.com", "senha")
				).get();
		
		// When
			String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);
			HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);

			ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
			    BASE_URL + "/" + usuario.getId(), HttpMethod.GET, requisicao, Usuario.class
			);
		
		// Then
			assertEquals(HttpStatus.OK, resposta.getStatusCode());
			assertNotNull(resposta.getBody());
	}

}
