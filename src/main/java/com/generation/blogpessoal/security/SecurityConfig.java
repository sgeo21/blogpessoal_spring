package com.generation.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// Endpoints públicos que não requerem autenticação
	private static final String[] PUBLIC_ENDPOINTS = { "/usuarios/logar", "/usuarios/cadastrar", "/error/**", "/",
			"/docs", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**" }; // Adicione outros endpoints públicos conforme necessário
	// Filtro de autenticação JWT
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	// Configuração do PasswordEncoder usando BCrypt - define a criptografia das senhas
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	// Configuração do AuthenticationManager
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	// Configuração da cadeia de filtros de segurança  ele define as configurações o que é permitidos e exige autenticação
	@Bean // chamada assim pois ela pode ser colocada em qualquer momento da aplicação
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(csrf -> csrf.disable()).cors(cors -> {
				})
				.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
						.requestMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
}
