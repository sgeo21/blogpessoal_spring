package com.generation.blogpessoal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	
	@Override // método que filtra cada requisição HTTP
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = extractTokenFromRequest(request);
			if (token == null || SecurityContextHolder.getContext().getAuthentication() != null) {
				filterChain.doFilter(request, response);
				return;
			}
			processJwtAuthentication(request, token);
			filterChain.doFilter(request, response); // continua a cadeia de filtros
		} catch (ExpiredJwtException | SignatureException | MalformedJwtException | UsernameNotFoundException e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
	}
	
	// Extrai o token JWT do cabeçalho Authorization
	private String extractTokenFromRequest(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
			return authHeader.substring(7);
		}
		return null;
	}

	// Processa a autenticação JWT
	private void processJwtAuthentication(HttpServletRequest request, String token) {
		String username = jwtService.extractUsername(token); // extrai o nome de usuário do token
		if (username != null && !username.trim().isEmpty()) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username); //carrega os detalhes do usuário
			if (jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities()); // cria o token de autenticação
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // adiciona detalhes da requisição
				SecurityContextHolder.getContext().setAuthentication(authToken); // seta a autenticação no contexto de segurança
			} else {
				throw new RuntimeException("Token JWT inválido ou expirado");
			} //avisa que não conseguiu extrair o usuário
		} else {
			throw new RuntimeException("Usuário não pode ser extraído do token JWT");
		} //avisa que o token é inválido
	}

}