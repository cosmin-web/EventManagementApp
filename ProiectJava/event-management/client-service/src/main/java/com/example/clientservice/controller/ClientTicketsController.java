package com.example.clientservice.controller;

import com.example.clientservice.integration.TicketData;
import com.example.clientservice.service.ClientTicketsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client-service/clients")
public class ClientTicketsController {

    private final ClientTicketsService ticketsService;

    public ClientTicketsController(ClientTicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    @PostMapping("/{email}/tickets/validate")
    public ResponseEntity<TicketData> validateTicket(
            @PathVariable String email,
            @RequestParam String cod,
            @RequestParam(defaultValue = "false") boolean save)
    {
        return ticketsService.validateTicket(email, cod, save)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{email}/tickets")
    public ResponseEntity<List<TicketData>> getClientTickets(@PathVariable String email) {
        List<TicketData> list = ticketsService.listDetailedTickets(email);
        return ResponseEntity.ok(list);
    }
}
