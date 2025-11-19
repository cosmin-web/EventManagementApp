package com.example.clientservice.controller;

import com.example.clientservice.integration.TicketData;
import com.example.clientservice.service.ClientTicketsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client-service/clients")
@Tag(name = "Client Tickets", description = "Operatii pentru validarea si listarea biletelor")
public class ClientTicketsController {

    private final ClientTicketsService ticketsService;

    public ClientTicketsController(ClientTicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    private Map<String, Object> wrap(Object data, Map<String, String> links) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("data", data);
        resp.put("links", links);
        return resp;
    }

    private Map<String, String> ticketLinks(String email) {
        return Map.of(
                "self", "/api/client-service/clients/" + email + "/tickets",
                "parent", "/api/client-service/clients"
        );
    }

    @Operation(summary = "Valideaza un bilet si optional il salveaza la client")
    @ApiResponse(responseCode = "200", description = "Bilet valid.")
    @ApiResponse(responseCode = "400", description = "Bilet invalid.")
    @PostMapping("/{email}/tickets/validate")
    public ResponseEntity<Map<String, Object>> validateTicket(
            @PathVariable String email,
            @RequestParam String cod,
            @RequestParam(defaultValue = "false") boolean save) {

        return ticketsService.validateTicket(email, cod, save)
                .map(data -> ResponseEntity.ok(wrap(data, ticketLinks(email))))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Returneaza toate biletele clientului")
    @ApiResponse(responseCode = "200", description = "Lista de bilete returnata.")
    @ApiResponse(responseCode = "404", description = "Clientul nu exista.")
    @GetMapping("/{email}/tickets")
    public ResponseEntity<Map<String, Object>> getClientTickets(@PathVariable String email) {
        List<TicketData> list = ticketsService.listDetailedTickets(email);
        return ResponseEntity.ok(wrap(list, ticketLinks(email)));
    }
}
