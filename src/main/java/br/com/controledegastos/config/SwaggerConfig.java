package br.com.controledegastos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Controle de Gastos API")
                        .version("3.0.0")
                        .description("API REST para gerenciamento de lancamentos financeiros. Interface web disponivel em /"));
    }
}
