package com.example.clientservice.infrastructure.adapter.in.rest;

import com.example.clientservice.application.mapper.ClientMapper;
import com.example.clientservice.domain.model.ClientDocument;
import com.example.clientservice.application.service.ClientService;
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
@Tag(name = "Public Clients", description = "Acces public la informatii despre clienti care au bilete")
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

    @Operation(summary = "Lista paginata de clienti publici care au bilete la un eveniment")
    @ApiResponse(responseCode = "200", description = "Lista de clienti a fost returnata.")
    @GetMapping("/by-event/{eventId}")
    public ResponseEntity<Map<String, Object>> getClientsByEvent(
            @PathVariable Integer eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5", name = "items_per_page") int size) {

        List<ClientDocument> all = clientService.findClientsByEventId(eventId)
                .stream()
                .filter(ClientDocument::isPublic)
                .toList();

        int start = page * size;
        int end = Math.min(start + size, all.size());

        List<ClientDocument> paginated =
                start >= all.size() ? List.of() : all.subList(start, end);

        var content = paginated.stream()
                .map(ClientMapper::toPublicDTO)
                .map(dto -> wrap(dto, publicLinks(dto.getId())))
                .toList();

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("content", content);
        resp.put("currentPage", page);
        resp.put("totalItems", all.size());
        resp.put("totalPages", (int) Math.ceil(all.size() / (double) size));

        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Lista paginata de clienti publici care au bilete pentru un pachet")
    @ApiResponse(responseCode = "200", description = "Lista de clienti a fost returnata.")
    @GetMapping("/by-package/{packageId}")
    public ResponseEntity<Map<String, Object>> getClientsByPackage(
            @PathVariable Integer packageId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5", name = "items_per_page") int size) {

        List<ClientDocument> all = clientService.findClientsByPackageId(packageId)
                .stream()
                .filter(ClientDocument::isPublic)
                .toList();

        int start = page * size;
        int end = Math.min(start + size, all.size());

        List<ClientDocument> paginated =
                start >= all.size() ? List.of() : all.subList(start, end);

        var content = paginated.stream()
                .map(ClientMapper::toPublicDTO)
                .map(dto -> wrap(dto, publicLinks(dto.getId())))
                .toList();

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("content", content);
        resp.put("currentPage", page);
        resp.put("totalItems", all.size());
        resp.put("totalPages", (int) Math.ceil(all.size() / (double) size));

        return ResponseEntity.ok(resp);
    }
}
