package com.hisabpro.backend.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}