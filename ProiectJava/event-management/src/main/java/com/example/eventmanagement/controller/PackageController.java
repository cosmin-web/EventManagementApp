package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.service.PackageService;
import jakarta.persistence.criteria.CriteriaBuilder;
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

    private Map<String, Object> wrapWithLinks(Object data, Map<String, String> links) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("data", data);
        resp.put("links", links);
        return resp;
    }

    private Map<String, String> packageLinks(Integer id) {
        return Map.of(
          "self", "/api/event-manager/event-packets" + id,
          "parent", "/api/event-manager/event-packets"
        );
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPackages() {
        var list = packageService.getAllPackages().stream()
                .map(p-> wrapWithLinks(p, packageLinks(p.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/id")
    public ResponseEntity<Map<String, Object>> getPackageById(@PathVariable Integer id) {
        return packageService.getPackageById(id)
                .map(p->ResponseEntity.ok(wrapWithLinks(p, packageLinks(p.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPackage(@RequestBody PackageEntity p) {
        PackageEntity saved = packageService.createPackages(p);
        return ResponseEntity.created(URI.create("/api/event-manager/event-packets/" + saved.getId()))
                .body(wrapWithLinks(saved, packageLinks(saved.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePackage(@PathVariable Integer id, @RequestBody PackageEntity p) {
        PackageEntity updated = packageService.updatePackage(id, p);
        return ResponseEntity.ok(wrapWithLinks(updated, packageLinks(updated.getId())));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deletePackage(@PathVariable Integer id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
}
