package com.thropic.talki.identity.application.services;

import com.thropic.talki.identity.domain.model.AppUser;
import com.thropic.talki.identity.infrastructure.persistence.AppUserRepository;
import com.thropic.talki.identity.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private AppUser sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new AppUser("manuel@upc.edu.pe", "hashed_password", "ManuelTumi", "ciclos_6_10");
    }

    @Test
    void register_whenEmailIsNew_shouldSaveAndReturnUser() {
        when(userRepository.existsByEmail("manuel@upc.edu.pe")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");
        when(userRepository.save(any(AppUser.class))).thenReturn(sampleUser);

        AppUser result = authService.register("manuel@upc.edu.pe", "password123", "ManuelTumi", "ciclos_6_10");

        assertThat(result.getEmail()).isEqualTo("manuel@upc.edu.pe");
        assertThat(result.getUsername()).isEqualTo("ManuelTumi");
        verify(userRepository).save(any(AppUser.class));
    }

    @Test
    void register_whenEmailAlreadyExists_shouldThrowException() {
        when(userRepository.existsByEmail("manuel@upc.edu.pe")).thenReturn(true);

        assertThatThrownBy(() ->
                authService.register("manuel@upc.edu.pe", "password123", "ManuelTumi", "ciclos_6_10")
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Email already registered");

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_whenCredentialsAreValid_shouldReturnToken() {
        when(userRepository.findByEmail("manuel@upc.edu.pe")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
        when(jwtTokenProvider.generateToken(anyString(), anyString())).thenReturn("jwt-token-abc");

        String token = authService.login("manuel@upc.edu.pe", "password123");

        assertThat(token).isEqualTo("jwt-token-abc");
    }

    @Test
    void login_whenUserNotFound_shouldThrowException() {
        when(userRepository.findByEmail("noexiste@upc.edu.pe")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login("noexiste@upc.edu.pe", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void login_whenPasswordIsWrong_shouldThrowException() {
        when(userRepository.findByEmail("manuel@upc.edu.pe")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("wrong_password", "hashed_password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login("manuel@upc.edu.pe", "wrong_password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    void register_whenAcademicSegmentProvided_shouldPersistIt() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");
        AppUser saved = new AppUser("a@b.com", "hash", "Gael", "ciclos_1_5");
        when(userRepository.save(any())).thenReturn(saved);

        AppUser result = authService.register("a@b.com", "pass", "Gael", "ciclos_1_5");

        assertThat(result.getAcademicSegment()).isEqualTo("ciclos_1_5");
    }
}
