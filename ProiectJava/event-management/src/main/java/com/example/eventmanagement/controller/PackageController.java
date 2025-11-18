package com.example.eventmanagement.controller;

import com.example.eventmanagement.dto.client.PublicClientDTO;
import com.example.eventmanagement.dto.PackageDTO;
import com.example.eventmanagement.integration.ClientApiClient;
import com.example.eventmanagement.mapper.PackageMapper;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.UserEntity;
import com.example.eventmanagement.service.PackageService;
import com.example.eventmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.checkerframework.checker.units.qual.A;
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
@Tag(name="Packages", description = "Operatii pentru gestionarea pachetelor de evenimente.")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientApiClient clientApiClient;

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

    private PackageDTO enrichPackage(PackageEntity entity) {
        PackageDTO dto = PackageMapper.fromEntity(entity);

        if(entity.getOwner() != null) {
            dto.setOwnerEmail(entity.getOwner().getEmail());
        }

        int countEvents = packageService.countEventsInPackage(entity);
        dto.setNumberOfEvents(countEvents);

        int countAvailableTickets = packageService.calculeazaLocuriDisponibile(entity);
        dto.setAvailableTickets(countAvailableTickets);

        return dto;
    }

    @Operation(summary = "Listare pachete", description = "Returneaza o lista paginata de pachete.")
    @ApiResponse(responseCode = "200", description = "Lista de pachete a fost returnata.")
    @ApiResponse(responseCode = "400", description = "Parametri de filtrare invalizi.")
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
                .map(p -> wrap(enrichPackage(p), packageLinks(p.getId())))
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", data);
        response.put("currentPage", resultPage.getNumber());
        response.put("totalItems", resultPage.getTotalElements());
        response.put("totalPage", resultPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtine un pachet dupa ID")
    @ApiResponse(responseCode = "200", description = "Pachetul a fost gasit.")
    @ApiResponse(responseCode = "404", description = "Pachetul nu a fost gasit.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPackageById(@PathVariable Integer id) {
        return packageService.getPackageById(id)
                .map(p->ResponseEntity.ok(wrap(enrichPackage(p), packageLinks(p.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creeaza un pachet nou")
    @ApiResponse(responseCode = "201", description = "Pachetul a fost creat cu succes.")
    @ApiResponse(responseCode = "400", description = "Date invalide sau lipsa de campuri obligatorii.")
    @ApiResponse(responseCode = "404", description = "Proprietarul specificat nu a fost gasit.")
    @ApiResponse(responseCode = "409", description = "Exista deja un pachet cu acelasi nume.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPackage(@RequestBody @Valid PackageDTO dto) {
        UserEntity owner = userService.getUserById(dto.getOwnerId())
                .orElseThrow(()->new IllegalArgumentException("Prorpietarul nu exista"));

        PackageEntity entity = PackageMapper.toEntity(dto, owner);
        PackageEntity saved = packageService.createPackages(entity);
        PackageDTO response = enrichPackage(saved);

        return ResponseEntity.created(URI.create("/api/event-manager/event-packets/" + saved.getId()))
                .body(wrap(response, packageLinks(saved.getId())));
    }

    @Operation(summary = "Actualizeaza un pachet existent")
    @ApiResponse(responseCode = "200", description = "Pachetul a fost actualizat cu succes.")
    @ApiResponse(responseCode = "400", description = "Date invalide.")
    @ApiResponse(responseCode = "404", description = "Pachetul nu a fost gasit.")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePackage(@PathVariable Integer id, @RequestBody @Valid PackageDTO dto) {
        UserEntity owner = userService.getUserById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Proprietarul nu exista"));

        PackageEntity entity = PackageMapper.toEntity(dto, owner);
        PackageEntity updated = packageService.updatePackage(id, entity);
        PackageDTO response = enrichPackage(updated);

        return ResponseEntity.ok(wrap(response, packageLinks(updated.getId())));
    }

    @Operation(summary = "Sterge un pachet")
    @ApiResponse(responseCode = "204", description = "Pachetul a fost sters.")
    @ApiResponse(responseCode = "404", description = "Pachetul nu a fost gasit.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Integer id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{packageId}/clients")
    public ResponseEntity<List<PublicClientDTO>> getClientsForPackage(
            @PathVariable Integer packageId,
            @RequestParam Integer ownerId) {

        var pkg = packageService.getPackageById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Pachetul nu există."));

        if (!pkg.getOwner().getId().equals(ownerId)) {
            return ResponseEntity.status(403).build();
        }

        List<PublicClientDTO> clients = clientApiClient.getClientsByPackage(packageId);
        return ResponseEntity.ok(clients);
    }

}
