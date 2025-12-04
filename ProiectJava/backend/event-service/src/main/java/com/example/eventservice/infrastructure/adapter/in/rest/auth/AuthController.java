package com.example.eventservice.infrastructure.adapter.in.rest.auth;

import com.example.eventservice.infrastructure.adapter.out.idm.IdmAuthClient;
import com.example.idm.grpc.LoginResponse;
import com.example.idm.grpc.LogoutResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.eventservice.application.dto.login.LoginDTO;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

public class AuthController {

    private final IdmAuthClient idmAuthClient;

    public AuthController(IdmAuthClient idmAuthClient) {
        this.idmAuthClient = idmAuthClient;
    }

    @PostMapping("/login")
    @Operation(summary = "Autentificare utilizator")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        LoginResponse resp = idmAuthClient.login(dto.getEmail(), dto.getParola());

        if (!resp.getSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", resp.getMessage()));
        }

        return ResponseEntity.ok(Map.of(
                "token", resp.getToken(),
                "message", resp.getMessage()
        ));
    }

    @PostMapping("/logout")
    @Operation(summary = "Deconectare utilizator, invalidare token")
    public ResponseEntity<?> logout(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Missing or invalid Authorization header"));
        }

        String token = authorizationHeader.substring("Bearer ".length());
        LogoutResponse resp = idmAuthClient.logout(token);

        return ResponseEntity.ok(Map.of(
                "success", resp.getSuccess(),
                "message", resp.getMessage()
        ));
    }
}
