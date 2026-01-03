package com.example.eventservice.infrastructure.adapter.in.rest;

import com.example.eventservice.infrastructure.adapter.out.idm.IdmAuthClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.idm.grpc.LoginResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IdmAuthClient idmAuthClient;

    public AuthController(IdmAuthClient idmAuthClient) {
        this.idmAuthClient = idmAuthClient;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        LoginResponse response = idmAuthClient.login(email, password);

        if (response.getSuccess()) {
            return ResponseEntity.ok(Map.of("token", response.getToken()));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Login failed"));
        }
    }
}
