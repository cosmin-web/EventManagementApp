package com.example.eventmanagement.controller;

import com.example.eventmanagement.dto.PackageDTO;
import com.example.eventmanagement.mapper.PackageMapper;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.UserEntity;
import com.example.eventmanagement.service.PackageService;
import com.example.eventmanagement.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-manager/event-packets")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private UserService userService;

    private Map<String, Object> wrap(Object data, Map<String, String> links) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("data", data);
        resp.put("links", links);
        return resp;
    }

    private Map<String, String> packageLinks(Integer id) {
        return Map.of(
          "self", "/api/event-manager/event-packets/" + id,
          "parent", "/api/event-manager/event-packets"
        );
    }

//    @GetMapping
//    public ResponseEntity<List<Map<String, Object>>> getAllPackages() {
//        var list = packageService.getAllPackages().stream()
//                .map(PackageMapper::fromEntity)
//                .map(dto -> wrap(dto, packageLinks(dto.getId())))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(list);
//    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPackages(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String eventName,
        @RequestParam (required = false, name = "available_tickets") Integer availableTickets,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5", name = "items_per_page") int size)
    {
        var resultPage = packageService.searchPackages(name, type, eventName, availableTickets, page, size);

        var data = resultPage.getContent().stream()
                .map(PackageMapper::fromEntity)
                .map(dto -> wrap(dto, packageLinks(dto.getId())))
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", data);
        response.put("currentPage", resultPage.getNumber());
        response.put("totalItems", resultPage.getTotalElements());
        response.put("totalPage", resultPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPackageById(@PathVariable Integer id) {
        return packageService.getPackageById(id)
                .map(PackageMapper::fromEntity)
                .map(dto->ResponseEntity.ok(wrap(dto, packageLinks(dto.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPackage(@RequestBody @Valid PackageDTO dto) {
        UserEntity owner = userService.getUserById(dto.getOwnerId())
                .orElseThrow(()->new IllegalArgumentException("Prorpietarul nu exista"));

        PackageEntity entity = PackageMapper.toEntity(dto, owner);
        PackageEntity saved = packageService.createPackages(entity);
        PackageDTO response = PackageMapper.fromEntity(saved);

        return ResponseEntity.created(URI.create("/api/event-manager/event-packets/" + saved.getId()))
                .body(wrap(response, packageLinks(saved.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePackage(@PathVariable Integer id, @RequestBody @Valid PackageDTO dto) {
        UserEntity owner = userService.getUserById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Proprietarul nu exista"));

        PackageEntity entity = PackageMapper.toEntity(dto, owner);
        PackageEntity updated = packageService.updatePackage(id, entity);
        PackageDTO response = PackageMapper.fromEntity(updated);

        return ResponseEntity.ok(wrap(response, packageLinks(updated.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Integer id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
}
