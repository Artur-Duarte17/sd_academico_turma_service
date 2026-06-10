package br.edu.ifgoiano.academico.sd_academico_turma_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger.
 *
 * Após subir o serviço, a documentação interativa fica disponível em:
 *   - Swagger UI:   http://localhost:8084/swagger-ui.html
 *   - OpenAPI JSON: http://localhost:8084/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI turmaServiceOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("/turma").description("Via API Gateway"),
                        new Server().url("/").description("Acesso direto ao serviço")))
                .info(new Info()
                .title("Turma Service API")
                .description("API de cadastro, consulta e controle de vagas de turmas do Sistema Acadêmico Distribuído.")
                .version("v1"));
    }
}
