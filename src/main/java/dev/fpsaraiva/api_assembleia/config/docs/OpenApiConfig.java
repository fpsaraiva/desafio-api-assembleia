package dev.fpsaraiva.api_assembleia.config.docs;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Assembleia")
                        .version("1.0")
                        .description("Documentação da API para o sistema de assembleia."));
    }
}
