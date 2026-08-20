package com.hisabpro.backend.dto.auth;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}