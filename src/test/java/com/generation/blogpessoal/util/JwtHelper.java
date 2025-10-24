package com.generation.blogpessoal.util;
 
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
 
import com.generation.blogpessoal.model.UsuarioLogin;
 
public class JwtHelper {
 
	private JwtHelper() {} //Método Construtor vazio e privado para impedir instanciação
		
	public static String obterToken(TestRestTemplate testRestTemplate, String usuario, String senha) {/*Recebe uma requisição HTTP, um usuário e uma senha.
	Serve para */
		UsuarioLogin usuarioLogin = TestBuilder.criarUsuarioLogin(usuario, senha); //Cria um objeto do tipo UsuarioLogin com os dados recebidos
	
		//Criando a requisição HTTP com o objeto usuarioLogin no corpo da requisição
		HttpEntity<UsuarioLogin> requisicao = new HttpEntity<UsuarioLogin>(usuarioLogin);
		
		//Enviar a Requisição para o endpoint /usuarios/logar e obter a resposta
		ResponseEntity<UsuarioLogin> resposta = testRestTemplate
				.exchange("/usuarios/logar", HttpMethod.POST, requisicao, UsuarioLogin.class);
		
		//Objeto UsuarioLogun para verificar o corpo da resposta - Se houver, testa a aplicação e retorna o token
		UsuarioLogin corpoResposta = resposta.getBody();
		
		if(corpoResposta != null && corpoResposta.getToken() != null) {
			return corpoResposta.getToken();
		}
		
		throw new RuntimeException("Falha no login: " + usuario);
		
	}
// Criando Método para
	
	public static <T> HttpEntity<T> criarRequisicaoComToken(T corpo, String token) {
		/*O <T> indica que o método é genérico e pode aceitar qualquer tipo de objeto como parâmetro, ou seja: nós é quem vamos definir se o tipo será
		 * String, double etc.*/
		HttpHeaders cabecalho = new HttpHeaders();
		String tokenLimpo = token.startsWith("Bearer ") ? token.substring(7): token;
		cabecalho.setBearerAuth(tokenLimpo);
		return new HttpEntity<>(corpo, cabecalho);
	}
		
	public static HttpEntity<Void> criarRequisicaoComToken(String token) {
		return criarRequisicaoComToken(null, token);
		//T = Type -> tema, postagem, usuário etc.
		//É void porque a requisição Http em si não retorna nada, mas o método retorna um HttpEntity
	}
}
