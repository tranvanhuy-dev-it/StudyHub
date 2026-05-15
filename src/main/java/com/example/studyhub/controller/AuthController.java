package com.example.studyhub.controller;

import com.example.studyhub.dto.request.LoginRequest;
import com.example.studyhub.dto.request.RegisterRequest;
import com.example.studyhub.dto.response.LoginResponse;
import com.example.studyhub.entities.User;
import com.example.studyhub.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Register success");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse res = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(res);
    }
}