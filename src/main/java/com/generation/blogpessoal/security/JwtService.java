package com.generation.blogpessoal.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component // gerenciada pelo spring podendo injetar coisas no construtor
public class JwtService {
	// é uma chave de assinatura aleatória incriptada do tipo
	private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	// tempo de expiração do token
	private static final Duration EXPIRATION_DURATION = Duration.ofMinutes(60);
	// assinatura do token
	private final SecretKey signingKey;

	// metodo construtor que gera array de bytes da chave secreta
	public JwtService() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		this.signingKey = Keys.hmacShaKeyFor(keyBytes);
	}

	// extrai todas as claims do token
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
			.verifyWith(signingKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	// extrai o nome de usuário do token
	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	// extrai o tempo de expiração do token
	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	// valida o token comparando o nome de usuário e a data de expiração
	public boolean validateToken(String token, UserDetails userDetails) {
		Claims claims = extractAllClaims(token);
		return claims.getSubject().equals(userDetails.getUsername()) && claims.getExpiration().after(new Date());
	}

	// gera um novo token para o nome de usuário fornecido
	public String generateToken(String username) {
		Instant now = Instant.now();
		return Jwts.builder().subject(username).issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(EXPIRATION_DURATION)))
				.signWith(signingKey)
				.compact();
	}
}