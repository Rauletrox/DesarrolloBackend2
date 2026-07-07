package com.minimarket.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI minimarketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Minimarket Plus API")
                        .version("1.0")
                        .description("Documentación de los servicios REST del sistema Minimarket Plus")
                        .contact(new Contact()
                                .name("Equipo de desarrollo")
                                .email("desarrollo@minimarketplus.local")));
    }
}
