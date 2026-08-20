package com.hisabpro.backend.controller;

import com.hisabpro.backend.dto.auth.LoginRequest;
import com.hisabpro.backend.dto.auth.LoginResponse;
import com.hisabpro.backend.dto.auth.LogoutRequest;
import com.hisabpro.backend.dto.auth.RefreshTokenRequest;
import com.hisabpro.backend.dto.user.UserResponse;
import com.hisabpro.backend.service.AuthService;
import com.hisabpro.backend.service.CurrentUserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;

    public AuthController(
            AuthService authService,
            CurrentUserService currentUserService
    ) {
        this.authService = authService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        System.out.println(
                ">>> LOGIN CONTROLLER HIT: " + request.email()
        );

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody LogoutRequest request
    ) {
        authService.logout(request.refreshToken());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(
                authService.refresh(request.refreshToken())
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {

        return ResponseEntity.ok(
                UserResponse.from(
                        currentUserService.getCurrentUser()
                )
        );
    }
}