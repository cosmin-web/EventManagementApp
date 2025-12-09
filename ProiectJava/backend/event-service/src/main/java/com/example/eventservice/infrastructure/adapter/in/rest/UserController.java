package com.example.eventservice.infrastructure.adapter.in.rest;

import com.example.eventservice.application.auth.AuthenticatedUser;
import com.example.eventservice.application.dto.UserDTO;
import com.example.eventservice.application.mapper.UserMapper;
import com.example.eventservice.application.auth.AuthorizationService;
import com.example.eventservice.domain.model.UserEntity;
import com.example.eventservice.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-manager/users")
@Tag(name = "Users", description = "Operatii pentru gestionarea utilizatorilor sistemului")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    private Map<String, Object> wrap(Object data, Map<String, String> links) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", data);
        response.put("links", links);
        return response;
    }


    private Map<String, String> userLinks(Integer id) {
        return Map.of(
                "self", "/api/event-manager/users/" + id,
                "parent", "/api/event-manager/users"
        );
    }

    @Operation(summary = "Listare utilizatori", description = "Returneaza o lista cu utilizatorii.")
    @ApiResponse(responseCode = "200", description = "Lista utilizatorilor a fost returnata.")
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader
    ) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.ADMIN
        );

        var list = userService.getAllUsers2().stream()
                .map(UserMapper::fromEntity)
                .map(u -> wrap(u, userLinks(u.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtine un utilizator după ID")
    @ApiResponse(responseCode = "200", description = "Utilizatorul a fost gasit.")
    @ApiResponse(responseCode = "404", description = "Utilizatorul nu a fost gasit.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(
            @PathVariable Integer id,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader
    ) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.ADMIN
        );

        return userService.getUserById(id)
                .map(UserMapper::fromEntity)
                .map(u -> ResponseEntity.ok(wrap(u, userLinks(u.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Creeaza un utilizator nou")
    @ApiResponse(responseCode = "201", description = "Utilizatorul a fost creat.")
    @ApiResponse(responseCode = "400", description = "Date invalide sau lipsa de campuri obligatorii.")
    @ApiResponse(responseCode = "409", description = "Emailul este deja utilizat.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestBody @Valid UserDTO dto,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.ADMIN
        );

        UserEntity entity = UserMapper.toEntity(dto);
        UserEntity saved = userService.createUser(entity);
        UserDTO response = UserMapper.fromEntity(saved);

        return ResponseEntity.created(URI.create("/api/event-manager/users/" + saved.getId()))
                .body(wrap(response, userLinks(saved.getId())));
    }

    @Operation(summary = "Actualizeaza un utilizator")
    @ApiResponse(responseCode = "200", description = "Utilizatorul a fost actualizat.")
    @ApiResponse(responseCode = "400", description = "Date invalide.")
    @ApiResponse(responseCode = "404", description = "Utilizatorul nu a fost gasit.")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserDTO dto,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.ADMIN
        );

        UserEntity updatedEntity = UserMapper.toEntity(dto);
        UserEntity saved = userService.updateUser(id, updatedEntity);
        UserDTO response = UserMapper.fromEntity(saved);

        return ResponseEntity.ok(wrap(response, userLinks(saved.getId())));
    }

    @Operation(summary = "Sterge un utilizator")
    @ApiResponse(responseCode = "204", description = "Utilizatorul a fost sters.")
    @ApiResponse(responseCode = "404", description = "Utilizatorul nu a fost gasit.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Integer id,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.ADMIN
        );

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
