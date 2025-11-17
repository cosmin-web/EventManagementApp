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
public class ClientCrudController {

    private final ClientService clientService;

    public ClientCrudController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients(@RequestParam(required = false) String name) {
        List<ClientDocument> clients = clientService.findAlls(name);
        var list = clients.stream().map(ClientMapper::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable String id) {
        return clientService.findById(id)
                .map(ClientMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    public ResponseEntity<ClientDTO> getClientByEmail(@RequestParam String email) {
        return clientService.findByEmail(email)
                .map(ClientMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createOrUpdateByEmail(@RequestBody ClientDTO dto) {
        ClientDocument saved = clientService.updateByEmail(ClientMapper.toDocument(dto));
        ClientDTO savedDto = ClientMapper.toDTO(saved);

        return ResponseEntity.created(
                URI.create("/api/client-service/clients/" + savedDto.getId())
        ).body(savedDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable String id, @RequestBody ClientDTO dto) {
        ClientDocument saved = clientService.updateById(id, ClientMapper.toDocument(dto));
        return ResponseEntity.ok(ClientMapper.toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
