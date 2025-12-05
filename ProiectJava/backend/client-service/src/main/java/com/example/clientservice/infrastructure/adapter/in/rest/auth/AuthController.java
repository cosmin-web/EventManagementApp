package com.example.clientservice.infrastructure.adapter.in.rest.auth;

import com.example.clientservice.application.auth.LoginDTO;
import com.example.clientservice.infrastructure.adapter.out.idm.IdmAuthClient;
import com.example.idm.grpc.LoginResponse;
import com.example.idm.grpc.LogoutResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/client-service/auth")
@Tag(name = "Auth", description = "Autentificare utilizatori prin IDM")
public class AuthController {

    private final IdmAuthClient idmAuthClient;

    public AuthController(IdmAuthClient idmAuthClient) {
        this.idmAuthClient = idmAuthClient;
    }

    @Operation(summary = "Login", description = "Autentificare si obtinere token JWT")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        LoginResponse resp = idmAuthClient.login(dto.getEmail(), dto.getParola());

        if (!resp.getSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "error", resp.getMessage()
                    ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", resp.getToken(),
                "message", resp.getMessage()
        ));
    }

    @Operation(summary = "Logout", description = "Invalidare token curent")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "error", "Missing or invalid Authorization header"
                    ));
        }

        String token = authorizationHeader.substring("Bearer ".length()).trim();
        LogoutResponse resp = idmAuthClient.logout(token);

        if (!resp.getSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", resp.getMessage()
                    ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", resp.getMessage()
        ));
    }
}
