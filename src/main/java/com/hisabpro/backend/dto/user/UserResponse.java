package com.hisabpro.backend.dto.user;

import com.hisabpro.backend.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String role,
        UUID companyId
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCompany().getId()
        );
    }
}