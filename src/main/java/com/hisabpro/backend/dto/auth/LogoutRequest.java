package com.hisabpro.backend.dto.auth;

public record LogoutRequest(
        String refreshToken
) {
}