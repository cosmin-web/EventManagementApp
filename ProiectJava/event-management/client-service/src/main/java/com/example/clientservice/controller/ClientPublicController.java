package com.example.clientservice.controller;

import com.example.clientservice.dto.PublicClientDTO;
import com.example.clientservice.mapper.ClientMapper;
import com.example.clientservice.model.ClientDocument;
import com.example.clientservice.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client-service/clients/public")
public class ClientPublicController {

    private final ClientService clientService;

    public ClientPublicController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/by-event/{eventId}")
    public ResponseEntity<List<PublicClientDTO>> getClientsByEvent(@PathVariable Integer eventId) {

        List<ClientDocument> clients = clientService.findClientsByEventId(eventId);

        List<PublicClientDTO> list = clients.stream()
                .map(ClientMapper::toPublicDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-package/{packageId}")
    public ResponseEntity<List<PublicClientDTO>> getClientsByPackage(@PathVariable Integer packageId) {

        List<ClientDocument> clients = clientService.findClientsByPackageId(packageId);

        List<PublicClientDTO> list = clients.stream()
                .map(ClientMapper::toPublicDTO)
                .toList();

        return ResponseEntity.ok(list);
    }
}
