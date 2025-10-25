package com.generation.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig { //configuracao do Swagger
	 
	@Bean
    OpenAPI springBlogPessoalOpenAPI() { //definindo as informacoes do projeto
        return new OpenAPI() //retornando um OpenAPI - Definicoes da API
            .info(new Info() 
                .title("Projeto Blog Pessoal")
                .description("Projeto Blog Pessoal - Geovana Cazali")
                .version("v0.0.1")
                .license(new License()
                    .name("Geovana Cazali")
                    .url("com.generation.blogpessoal.BlogpessoalApplication"))
                .contact(new Contact()
                    .name("Geovana Cazali")
                    .url("https://github.com/sgeo21/blogpessoal_spring")
                    .email("s.geo2112@gmail.com")))
            .externalDocs(new ExternalDocumentation()
                .description("Github")
                .url("https://github.com/sgeo21"))
            .components(new Components()
                    .addSecuritySchemes("jwt_auth", createSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("jwt_auth"));
    }

	@Bean
	OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() { //adicionando respostas padrao para todas as APIs

		return openApi -> {
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> { //percorrendo todos os caminhos da API

				ApiResponses apiResponses = operation.getResponses(); // respostas da operacao

				apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
				apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
				apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
				apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
				apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
				apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido!"));
				apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
				apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));

			}));
		};
	}

	private ApiResponse createApiResponse(String message) { //criando a resposta da API

		return new ApiResponse().description(message); //retornando a mensagem

	}
	
	private SecurityScheme createSecurityScheme() { //criando o esquema de seguranca
	    return new SecurityScheme() //retornando um esquema de seguranca - definicoes de seguranca
	        .name("jwt_auth")
	        .type(SecurityScheme.Type.HTTP)
	        .scheme("bearer")
	        .bearerFormat("JWT")
	        .description("Insira apenas o token JWT (a palavra 'Bearer' será adicionada automaticamente)");
	}
}
