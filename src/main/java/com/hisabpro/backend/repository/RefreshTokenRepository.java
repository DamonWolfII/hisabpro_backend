package com.hisabpro.backend.repository;

import com.hisabpro.backend.entity.RefreshToken;
import com.hisabpro.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, UUID> {

    @Query("""
        SELECT rt
        FROM RefreshToken rt
        JOIN FETCH rt.user
        WHERE rt.token = :token
    """)
    Optional<RefreshToken> findByTokenWithUser(
            @Param("token") String token
    );

    void deleteByUser(User user);
}