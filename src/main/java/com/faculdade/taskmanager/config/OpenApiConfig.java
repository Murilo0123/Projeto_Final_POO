package com.faculdade.taskmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Springdoc OpenAPI (Swagger UI).
 * Documentação disponível em: /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .version("1.0.0")
                        .description("""
                                Microserviço RESTful para gerenciamento de tarefas e categorias.
                                
                                **Funcionalidades:**
                                - Criação, listagem, atualização e remoção de tarefas
                                - Categorização de tarefas
                                - Filtro de tarefas por status
                                - Atualização parcial de status via PATCH
                                
                                **Statuses disponíveis:** PENDING, IN_PROGRESS, DONE, CANCELLED
                                """)
                        .contact(new Contact()
                                .name("Grupo - Faculdade")
                                .email("grupo@faculdade.edu.br"))
                        .license(new License()
                                .name("MIT License")));
    }
}
