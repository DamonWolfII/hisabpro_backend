package com.hisabpro.backend.repository;

import com.hisabpro.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("""
        SELECT u
        FROM User u
        JOIN FETCH u.company
        WHERE u.email = :email
    """)
    Optional<User> findByEmailWithCompany(
            @Param("email") String email
    );

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}