package com.thropic.talki.notification.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3 para el notification-service.
 *
 * Publica la especificación en /v3/api-docs y la UI en /swagger-ui.html.
 * Cumple el criterio "RESTful API con documentación de cada servicio" de la
 * rúbrica TP1 del curso SI657. Aunque este servicio es predominantemente
 * consumidor de eventos AMQP (achievement.unlocked, scoring.completed), el
 * endpoint Actuator y los health checks se documentan vía OpenAPI para
 * trazabilidad uniforme con los demás microservicios.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Talki — Notification Service API")
                        .description("Microservicio consumidor de eventos del dominio Notification (DDD) "
                                + "del producto Talki. Procesa `scoring.completed` y `achievement.unlocked` "
                                + "del exchange `talki.events` y entrega notificaciones push al cliente "
                                + "Angular vía WebSocket. Forma parte del mono-repo talki-user-services.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Thropic — Equipo Talki")
                                .url("https://github.com/upc-pre-202601-si657-7940-thropic"))
                        .license(new License()
                                .name("Académico — UPC SI657 2026-10")))
                .servers(List.of(
                        new Server().url("http://localhost:8091").description("Local (perfil dev)"),
                        new Server().url("https://api.talki.app").description("Producción (Railway)")
                ));
    }
}
