package com.example.clientservice.controller;


import com.example.clientservice.dto.ClientDTO;
import com.example.clientservice.mapper.ClientMapper;
import com.example.clientservice.model.ClientDocument;
import com.example.clientservice.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/client-service/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients(@RequestParam(required = false) String name) {

        List<ClientDocument> clients = clientService.findAlls(name);
        var list = clients.stream()
                .map(ClientMapper::toDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{email}")
    public ResponseEntity<ClientDTO> getClientByEmail(@PathVariable String email) {
        return clientService.findByEmail(email)
                .map(ClientMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClientDTO> upClient(@RequestBody ClientDTO dto) {
        ClientDocument doc = ClientMapper.toDocument(dto);
        ClientDocument saved = clientService.updateClient(doc);
        ClientDTO savedDto = ClientMapper.toDTO(saved);

        return ResponseEntity.created(URI.create("/api/client-service/clients/" + savedDto.getEmail()))
                .body(savedDto);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteClient(@PathVariable String email) {
        clientService.delete(email);
        return ResponseEntity.noContent().build();
    }
}
