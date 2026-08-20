package com.hisabpro.backend.service;

import com.hisabpro.backend.dto.auth.LoginRequest;
import com.hisabpro.backend.dto.auth.LoginResponse;
import com.hisabpro.backend.entity.RefreshToken;
import com.hisabpro.backend.entity.User;
import com.hisabpro.backend.repository.UserRepository;
import com.hisabpro.backend.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!user.isActive()) {
            throw new BadCredentialsException(
                    "User account is inactive"
            );
        }

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new BadCredentialsException(
                    "Invalid email or password"
            );
        }

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken()
        );
    }

    public LoginResponse refresh(String token) {

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(token);

        User user = refreshToken.getUser();

        if (!user.isActive()) {
            throw new BadCredentialsException(
                    "User account is inactive"
            );
        }

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(
                accessToken,
                refreshToken.getToken()
        );
    }

    public void logout(String refreshToken) {
        refreshTokenService.revokeToken(refreshToken);
    }
}