package com.gv.user.management.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gv.user.management.auth.dto.AuthenticationRequest;
import com.gv.user.management.auth.dto.AuthenticationResponse;
import com.gv.user.management.auth.dto.RegisterRequest;
import com.gv.user.management.auth.dto.TokenValidationResponse;
import com.gv.user.management.constant.TokenType;
import com.gv.user.management.exception.UserAlreadyExistsException;
import com.gv.user.management.model.Token;
import com.gv.user.management.model.User;
import com.gv.user.management.repository.TokenRepository;
import com.gv.user.management.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    public AuthenticationResponse register(final RegisterRequest request) {
        final var user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        final Optional<User> byEmail = repository.findByEmail(request.getEmail());
        if (byEmail.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        final var savedUser = repository.save(user);
        final var jwtToken = jwtService.generateToken(user);
        final var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        final var user = repository.findByEmail(request.getEmail()).orElseThrow();
        final var jwtToken = jwtService.generateToken(user);
        final var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(final User user, final String jwtToken) {
        final var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(final User user) {
        final var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            final var user = this.repository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                final var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                final var authResponse = AuthenticationResponse.builder()
                        .userId(user.getId())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                objectMapper.writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public TokenValidationResponse validateToken(final String token) {
        final String userEmail = jwtService.extractUsername(token);
        if (userEmail == null) {
            return TokenValidationResponse.builder()
                    .isValid(false)
                    .message("Invalid token")
                    .build();
        }
        final var user = repository.findByEmail(userEmail).orElse(null);
        if (user == null || !jwtService.isTokenValid(token, user)) {
            return TokenValidationResponse.builder()
                    .isValid(false)
                    .message("Invalid or expired token")
                    .build();
        }
        return TokenValidationResponse.builder()
                .isValid(true)
                .message("Token is valid")
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .permissions(user.getRole().getAuthorities())
                .build();
    }
}
