package com.hisabpro.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );

        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(
            UUID userId,
            String email,
            String role
    ) {
        return generateToken(
                userId,
                email,
                role,
                accessTokenExpiration
        );
    }

    public String generateRefreshToken(UUID userId) {
        return generateToken(
                userId,
                null,
                null,
                refreshTokenExpiration
        );
    }

    private String generateToken(
            UUID userId,
            String email,
            String role,
            long expiration
    ) {
        Date now = new Date();
        Date expiry = new Date(
                now.getTime() + expiration
        );

        var builder = Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiry);

        if (email != null) {
            builder.claim("email", email);
        }

        if (role != null) {
            builder.claim("role", role);
        }

        return builder
                .signWith(secretKey)
                .compact();
    }

    public UUID extractUserId(String token) {
        Claims claims = extractAllClaims(token);

        return UUID.fromString(
                claims.getSubject()
        );
    }

    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);

        return claims.get(
                "email",
                String.class
        );
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);

        return claims.get(
                "role",
                String.class
        );
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}