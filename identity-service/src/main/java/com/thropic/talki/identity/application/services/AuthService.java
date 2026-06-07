package com.thropic.talki.identity.application.services;

import com.thropic.talki.identity.application.events.UserRegisteredEventPublisher;
import com.thropic.talki.identity.domain.event.UserRegisteredEvent;
import com.thropic.talki.identity.domain.model.AppUser;
import com.thropic.talki.identity.infrastructure.persistence.AppUserRepository;
import com.thropic.talki.identity.infrastructure.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRegisteredEventPublisher userRegisteredEventPublisher;

    public AuthService(AppUserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       UserRegisteredEventPublisher userRegisteredEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRegisteredEventPublisher = userRegisteredEventPublisher;
    }

    public AppUser register(String email, String rawPassword, String username, String academicSegment) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        String hash = passwordEncoder.encode(rawPassword);
        AppUser user = new AppUser(email, hash, username, academicSegment);
        AppUser saved = userRepository.save(user);

        // Saga por coreografía: notifica a los bounded contexts downstream
        // (session-service, etc.) para que proyecten localmente al usuario.
        userRegisteredEventPublisher.publish(new UserRegisteredEvent(
                saved.getId(),
                saved.getEmail(),
                saved.getUsername(),
                saved.getAcademicSegment()
        ));
        return saved;
    }

    public String login(String email, String rawPassword) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtTokenProvider.generateToken(email, user.getId().toString());
    }
}
