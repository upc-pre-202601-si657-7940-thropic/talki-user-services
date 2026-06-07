package com.thropic.talki.identity.domain.event;

/**
 * Evento de dominio publicado cuando se registra un nuevo usuario en
 * identity-service. Forma parte de la Saga por coreografía sobre el
 * exchange {@code talki.events} (routing key {@code user.registered}) y
 * permite que los bounded contexts downstream (p. ej. session-service)
 * mantengan su propia proyección local del usuario sin acoplarse al
 * esquema de identity-service (database-per-service).
 */
public class UserRegisteredEvent {

    private Long userId;
    private String email;
    private String username;
    private String academicSegment;

    public UserRegisteredEvent() {
    }

    public UserRegisteredEvent(Long userId, String email, String username, String academicSegment) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.academicSegment = academicSegment;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAcademicSegment() { return academicSegment; }
    public void setAcademicSegment(String academicSegment) { this.academicSegment = academicSegment; }
}
