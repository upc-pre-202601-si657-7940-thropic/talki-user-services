package com.thropic.talki.identity.application.events;

import com.thropic.talki.identity.domain.event.UserRegisteredEvent;

/**
 * Puerto de salida (Hexagonal / DDD) para publicar el evento de dominio
 * {@link UserRegisteredEvent}. La capa de aplicación depende de esta
 * abstracción; la implementación concreta (RabbitMQ) vive en infraestructura,
 * lo que mantiene AuthService desacoplado del broker y testeable con un doble.
 */
public interface UserRegisteredEventPublisher {

    void publish(UserRegisteredEvent event);
}
