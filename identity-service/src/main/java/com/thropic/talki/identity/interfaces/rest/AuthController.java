package com.thropic.talki.identity.interfaces.rest;

import com.thropic.talki.identity.application.services.AuthService;
import com.thropic.talki.identity.domain.model.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        AppUser user = authService.register(
                body.get("email"),
                body.get("password"),
                body.get("username"),
                body.getOrDefault("academicSegment", "ciclos_6_10")
        );
        return ResponseEntity.status(201).body(Map.of(
                "userId", user.getId(),
                "email", user.getEmail(),
                "username", user.getUsername(),
                "academicSegment", user.getAcademicSegment()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String token = authService.login(body.get("email"), body.get("password"));
        return ResponseEntity.ok(Map.of("token", token, "type", "Bearer"));
    }
}
