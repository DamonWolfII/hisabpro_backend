package com.hisabpro.backend.service;

import com.hisabpro.backend.entity.RefreshToken;
import com.hisabpro.backend.entity.User;
import com.hisabpro.backend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenExpiration;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(
                Instant.now().plusMillis(refreshTokenExpiration)
        );
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository.findByTokenWithUser(token)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Refresh token not found"
                                )
                        );

        if (refreshToken.isRevoked()) {
            throw new RuntimeException(
                    "Refresh token has been revoked"
            );
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }

    public void revokeToken(String token) {

        refreshTokenRepository.findByTokenWithUser(token)
                .ifPresent(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                });
    }

    public void revokeAllUserTokens(User user) {

        refreshTokenRepository.deleteByUser(user);
    }
}