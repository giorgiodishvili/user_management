package com.gv.user.management.auth.controller;

import com.gv.user.management.auth.dto.AuthenticationRequest;
import com.gv.user.management.auth.dto.AuthenticationResponse;
import com.gv.user.management.auth.dto.RegisterRequest;
import com.gv.user.management.auth.dto.TokenValidationResponse;
import com.gv.user.management.auth.service.AuthenticationService;
import com.gv.user.management.facade.UserServiceFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserServiceFacade userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid final RegisterRequest request) {
        //        request.setRole(Role.USER);
        return ResponseEntity.ok(userService.add(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid final AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @RequestHeader("Authorization") final String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(TokenValidationResponse.builder()
                            .isValid(false)
                            .message("Missing or invalid Authorization header")
                            .build());
        }

        final var token = authHeader.substring(7);
        final var response = service.validateToken(token);
        final var status = response.isValid() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).body(response);
    }
}
