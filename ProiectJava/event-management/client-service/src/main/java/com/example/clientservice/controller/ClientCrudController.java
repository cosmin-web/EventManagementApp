package com.example.clientservice.controller;

import com.example.clientservice.dto.ClientDTO;
import com.example.clientservice.mapper.ClientMapper;
import com.example.clientservice.model.ClientDocument;
import com.example.clientservice.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client-service/clients")
@Tag(name = "Clients", description = "Operatii pentru gestionarea clientilor")
public class ClientCrudController {

    private final ClientService clientService;

    public ClientCrudController(ClientService clientService) {
        this.clientService = clientService;
    }

    private Map<String, Object> wrap(Object data, Map<String, String> links) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("data", data);
        resp.put("links", links);
        return resp;
    }

    private Map<String, String> clientLinks(String id) {
        return Map.of(
                "self", "/api/client-service/clients/" + id,
                "parent", "/api/client-service/clients"
        );
    }


    @Operation(summary = "Listare clienti")
    @ApiResponse(responseCode = "200", description = "Lista de clienti a fost returnata.")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getClients(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5", name = "items_per_page") int size) {

        List<ClientDocument> all = clientService.findAlls(name);

        int start = page * size;
        int end = Math.min(start + size, all.size());

        List<ClientDocument> paginated =
                start >= all.size() ? List.of() : all.subList(start, end);

        var content = paginated.stream()
                .map(ClientMapper::toDTO)
                .map(dto -> wrap(dto, clientLinks(dto.getId())))
                .toList();

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("content", content);
        resp.put("currentPage", page);
        resp.put("totalItems", all.size());
        resp.put("totalPages", (int) Math.ceil(all.size() / (double) size));

        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Obtine un client dupa ID")
    @ApiResponse(responseCode = "200", description = "Clientul a fost gasit.")
    @ApiResponse(responseCode = "404", description = "Clientul nu exista.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getClientById(@PathVariable String id) {
        return clientService.findById(id)
                .map(ClientMapper::toDTO)
                .map(dto -> ResponseEntity.ok(wrap(dto, clientLinks(dto.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtine un client dupa email")
    @ApiResponse(responseCode = "200", description = "Clientul a fost gasit.")
    @ApiResponse(responseCode = "404", description = "Clientul nu exista.")
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getClientByEmail(@PathVariable String email) {
        return clientService.findByEmail(email)
                .map(ClientMapper::toDTO)
                .map(dto -> ResponseEntity.ok(wrap(dto, clientLinks(dto.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creeaza sau actualizeaza un client dupa email")
    @ApiResponse(responseCode = "201", description = "Client creat.")
    @ApiResponse(responseCode = "200", description = "Client actualizat.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrUpdateByEmail(@RequestBody ClientDTO dto) {
        ClientDocument saved = clientService.updateByEmail(ClientMapper.toDocument(dto));
        ClientDTO savedDto = ClientMapper.toDTO(saved);

        return ResponseEntity
                .created(URI.create("/api/client-service/clients/" + savedDto.getId()))
                .body(wrap(savedDto, clientLinks(savedDto.getId())));
    }

    @Operation(summary = "Actualizeaza un client dupa ID")
    @ApiResponse(responseCode = "200", description = "Client actualizat.")
    @ApiResponse(responseCode = "404", description = "Clientul nu exista.")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateClient(@PathVariable String id, @RequestBody ClientDTO dto) {
        ClientDocument updated = clientService.updateById(id, ClientMapper.toDocument(dto));
        ClientDTO result = ClientMapper.toDTO(updated);

        return ResponseEntity.ok(wrap(result, clientLinks(result.getId())));
    }

    @Operation(summary = "Sterge un client")
    @ApiResponse(responseCode = "204", description = "Clientul a fost sters.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
