package com.example.eventmanagement.controller;

import com.example.eventmanagement.dto.UserDTO;
import com.example.eventmanagement.mapper.UserMapper;
import com.example.eventmanagement.model.UserEntity;
import com.example.eventmanagement.service.UserService;
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
public class UserController {

    @Autowired
    private UserService userService;

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

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        var list = userService.getAllUsers().stream()
                .map(UserMapper::fromEntity)
                .map(u -> wrap(u, userLinks(u.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(UserMapper::fromEntity)
                .map(u -> ResponseEntity.ok(wrap(u, userLinks(u.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody @Valid UserDTO dto) {
        UserEntity entity = UserMapper.toEntity(dto, "defaultPassword");
        UserEntity saved = userService.createUser(entity);
        UserDTO response = UserMapper.fromEntity(saved);

        return ResponseEntity.created(URI.create("/api/event-manager/users/" + saved.getId()))
                .body(wrap(response, userLinks(saved.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Integer id,
                                                          @RequestBody @Valid UserDTO dto) {
        UserEntity updatedEntity = UserMapper.toEntity(dto, "defaultPassword");
        UserEntity saved = userService.updateUser(id, updatedEntity);
        UserDTO response = UserMapper.fromEntity(saved);

        return ResponseEntity.ok(wrap(response, userLinks(saved.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
