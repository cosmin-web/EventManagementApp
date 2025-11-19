package com.example.clientservice.controller;

import com.example.clientservice.dto.PublicClientDTO;
import com.example.clientservice.mapper.ClientMapper;
import com.example.clientservice.model.ClientDocument;
import com.example.clientservice.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client-service/clients/public")
@Tag(name = "Public Clients", description = "Acces public la datele clientilor")
public class ClientPublicController {

    private final ClientService clientService;

    public ClientPublicController(ClientService clientService) {
        this.clientService = clientService;
    }

    private Map<String, Object> wrap(Object data, Map<String, String> links) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("data", data);
        resp.put("links", links);
        return resp;
    }

    private Map<String, String> publicLinks(String id) {
        return Map.of(
                "self", "/api/client-service/clients/public/" + id,
                "parent", "/api/client-service/clients/public"
        );
    }

    @Operation(summary = "Clienti care au bilete pentru un eveniment")
    @ApiResponse(responseCode = "200", description = "Lista returnata.")
    @GetMapping("/by-event/{eventId}")
    public ResponseEntity<List<Map<String, Object>>> getClientsByEvent(@PathVariable Integer eventId) {
        List<ClientDocument> clients = clientService.findClientsByEventId(eventId);

        var data = clients.stream()
                .map(ClientMapper::toPublicDTO)
                .map(dto -> wrap(dto, publicLinks(dto.getId())))
                .toList();

        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Clienti care au bilete pentru un pachet")
    @ApiResponse(responseCode = "200", description = "Lista returnata.")
    @GetMapping("/by-package/{packageId}")
    public ResponseEntity<List<Map<String, Object>>> getClientsByPackage(@PathVariable Integer packageId) {

        List<ClientDocument> clients = clientService.findClientsByPackageId(packageId);

        var data = clients.stream()
                .map(ClientMapper::toPublicDTO)
                .map(dto -> wrap(dto, publicLinks(dto.getId())))
                .toList();

        return ResponseEntity.ok(data);
    }
}
