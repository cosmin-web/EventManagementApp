package com.example.clientservice.infrastructure.adapter.in.rest;

import com.example.clientservice.application.auth.AuthenticatedUser;
import com.example.clientservice.application.auth.AuthorizationService;
import com.example.clientservice.application.service.ClientService;
import com.example.clientservice.domain.model.UserRole;
import com.example.clientservice.infrastructure.adapter.out.event.dto.TicketData;
import com.example.clientservice.application.service.ClientTicketsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client-service/clients")
@Tag(name = "Client Tickets", description = "Operatii pentru validarea si listarea biletelor")
public class ClientTicketsController {

    @Autowired
    private ClientTicketsService ticketsService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ClientService clientService;

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
            @RequestParam(defaultValue = "false") boolean save,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserRole.CLIENT,
                UserRole.ADMIN
        );

        var client = clientService.findByEmail(email).orElse(null);

        if (current.getRole() == UserRole.CLIENT) {
            if (client == null || !email.equalsIgnoreCase(current.getEmail())) {
                return ResponseEntity.status(403).build();
            }
        }

        return ticketsService.validateTicket(email, cod, save, authorizationHeader)
                .map(data -> ResponseEntity.ok(wrap(data, ticketLinks(email))))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Lista paginata de bilete detaliate ale clientului")
    @ApiResponse(responseCode = "200", description = "Lista de bilete a fost returnata.")
    @GetMapping("/{email}/tickets")
    public ResponseEntity<Map<String, Object>> getClientTickets(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5", name = "items_per_page") int size,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserRole.CLIENT,
                UserRole.ADMIN
        );

        var client = clientService.findByEmail(email).orElse(null);

        if (current.getRole() == UserRole.CLIENT) {
            if (client == null || !email.equalsIgnoreCase(current.getEmail())) {
                return ResponseEntity.status(403).build();
            }
        }

        List<TicketData> all = ticketsService.listDetailedTickets(email, authorizationHeader);

        int start = page * size;
        int end = Math.min(start + size, all.size());

        List<TicketData> paginated =
                start >= all.size() ? List.of() : all.subList(start, end);

        var content = paginated.stream()
                .map(t -> wrap(t, ticketLinks(email)))
                .toList();

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("content", content);
        resp.put("currentPage", page);
        resp.put("totalItems", all.size());
        resp.put("totalPages", (int) Math.ceil(all.size() / (double) size));

        return ResponseEntity.ok(resp);
    }
}
