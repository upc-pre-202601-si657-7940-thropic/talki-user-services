package com.thropic.talki.identity.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3 para el identity-service.
 *
 * Publica la especificación en /v3/api-docs y la UI interactiva en
 * /swagger-ui.html, materializando el requisito de "RESTful API con
 * documentación" de la rúbrica TP1 del curso SI657 y la herramienta
 * Swagger declarada en la tabla 5.2.1 del informe.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI identityServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Talki — Identity Service API")
                        .description("API de autenticación y gestión de identidad del producto Talki. "
                                + "Implementa el bounded context Identity & Access (DDD) con JWT RSA-256, "
                                + "bcrypt cost ≥ 12 y refresh token rotation. Forma parte del mono-repo "
                                + "talki-user-services bajo la organización académica de UPC SI657-7940.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Thropic — Equipo Talki")
                                .url("https://github.com/upc-pre-202601-si657-7940-thropic"))
                        .license(new License()
                                .name("Académico — UPC SI657 2026-10")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local (perfil dev)"),
                        new Server().url("https://api.talki.lat").description("Producción (Railway)")
                ));
    }
}
