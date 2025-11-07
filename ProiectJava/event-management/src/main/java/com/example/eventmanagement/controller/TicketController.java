package com.example.eventmanagement.controller;

import com.example.eventmanagement.mapper.TicketMapper;
import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.TicketEntity;
import com.example.eventmanagement.service.EventService;
import com.example.eventmanagement.service.PackageService;
import com.example.eventmanagement.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-manager")
@Tag(name = "Tickets", description = "Operatii pentru gestionarea biletelor pentru evenimente si pachete")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EventService eventService;

    @Autowired
    private PackageService packageService;

    private Map<String, Object> wrap(Object data, Map<String, String> links) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("data", data);
        resp.put("links", links);
        return resp;
    }

    private Map<String, String> ticketLinks(String cod) {
        return Map.of(
                "self", "/api/event-manager/tickets/" + cod,
                "parent", "/api/event-manager/tickets"
        );
    }

    private Map<String, Object> enrichTicket(TicketEntity ticket) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("cod", ticket.getCod());

        if (ticket.getEveniment() != null) {
            var e = ticket.getEveniment();
            data.put("event", Map.of(
                    "id", e.getId(),
                    "nume", e.getNume(),
                    "locatie", e.getLocatie(),
                    "descriere", e.getDescriere(),
                    "numarLocuri", e.getNumarLocuri()
            ));
        }

        if (ticket.getPachet() != null) {
            var p = ticket.getPachet();
            data.put("package", Map.of(
                    "id", p.getId(),
                    "nume", p.getNume(),
                    "locatie", p.getLocatie(),
                    "descriere", p.getDescriere()
            ));
        }

        return data;
    }

    @Operation(summary = "Listare bilete", description = "Returneaza o lista paginata de bilete")
    @ApiResponse(responseCode = "200", description = "Lista biletelor a fost returnata.")
    @ApiResponse(responseCode = "400", description = "Parametri de filtrare invalizi.")
    @GetMapping("/tickets")
    public ResponseEntity<Map<String, Object>> getTickets(
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) String packageName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5", name = "items_per_page") int size) {

        var resultPage = ticketService.searchTickets(eventName, packageName, page, size);

        var data = resultPage.getContent().stream()
                .map(this::enrichTicket)
                .map(dto -> wrap(dto, ticketLinks((String) dto.get("cod"))))
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", data);
        response.put("currentPage", resultPage.getNumber());
        response.put("totalItems", resultPage.getTotalElements());
        response.put("totalPages", resultPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtine un bilet după cod")
    @ApiResponse(responseCode = "200", description = "Biletul a fost gasit.")
    @ApiResponse(responseCode = "404", description = "Biletul nu a fost gasit.")
    @GetMapping("/tickets/{cod}")
    public ResponseEntity<Map<String, Object>> getTicketByCod(@PathVariable String cod) {
        return ticketService.getTicketByCode(cod)
                .map(t -> ResponseEntity.ok(wrap(enrichTicket(t), ticketLinks(t.getCod()))))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Biletul nu exista")));
    }

    @Operation(summary = "Listare bilete pentru un eveniment")
    @ApiResponse(responseCode = "200", description = "Lista biletelor pentru eveniment a fost returnata.")
    @ApiResponse(responseCode = "404", description = "Evenimentul nu a fost gasit.")
    @GetMapping("/events/{eventId}/tickets")
    public ResponseEntity<List<Map<String, Object>>> getAllTicketsFromEvent(@PathVariable Integer eventId) {
        EventEntity eveniment = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista."));

        var list = ticketService.getTicketsByEvent(eveniment).stream()
                .map(this::enrichTicket)
                .map(dto -> wrap(dto, ticketLinks((String) dto.get("cod"))))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Listare bilete pentru un pachet")
    @ApiResponse(responseCode = "200", description = "Lista biletelor pentru pachet a fost returnata.")
    @ApiResponse(responseCode = "404", description = "Pachetul nu a fost gasit.")
    @GetMapping("/event-packets/{packetId}/tickets")
    public ResponseEntity<List<Map<String, Object>>> getAllTicketsFromPackage(@PathVariable Integer packetId) {
        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(() -> new IllegalArgumentException("Pachetul nu exista."));

        var list = ticketService.getTicketsByPackage(pachet).stream()
                .map(this::enrichTicket)
                .map(dto -> wrap(dto, ticketLinks((String) dto.get("cod"))))
                .toList();

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Creeaza un bilet pentru un eveniment")
    @ApiResponse(responseCode = "201", description = "Biletul a fost creat cu succes.")
    @ApiResponse(responseCode = "400", description = "Nu mai sunt locuri disponibile sau date invalide.")
    @ApiResponse(responseCode = "404", description = "Evenimentul nu a fost gasit.")
    @PostMapping("/events/{eventId}/tickets")
    public ResponseEntity<Map<String, Object>> createTicketForEvent(@PathVariable Integer eventId) {
        TicketEntity ticket = ticketService.createTicketForEvent(eventId);
        return ResponseEntity.created(URI.create("/api/event-manager/tickets/" + ticket.getCod()))
                .body(wrap(ticket, ticketLinks(ticket.getCod())));
    }

    @Operation(summary = "Creeaza un bilet pentru un pachet")
    @ApiResponse(responseCode = "201", description = "Biletul pentru pachet a fost creat cu succes.")
    @ApiResponse(responseCode = "400", description = "Nu mai sunt locuri disponibile sau date invalide.")
    @ApiResponse(responseCode = "404", description = "Pachetul nu a fost gasit.")
    @PostMapping("/event-packets/{packetId}/tickets")
    public ResponseEntity<Map<String, Object>> createTicketForPackage(@PathVariable Integer packetId) {
        TicketEntity ticket = ticketService.createTicketForPackage(packetId);
        return ResponseEntity.created(URI.create("/api/event-manager/tickets/" + ticket.getCod()))
                .body(wrap(ticket, ticketLinks(ticket.getCod())));
    }

    @Operation(summary = "Sterge un bilet dupa cod")
    @ApiResponse(responseCode = "204", description = "Biletul a fost sters.")
    @ApiResponse(responseCode = "404", description = "Biletul nu a fost gasit.")
    @DeleteMapping("/tickets/{cod}")
    public ResponseEntity<Void> deleteTicket(@PathVariable String cod) {
        ticketService.deleteTicket(cod);
        return ResponseEntity.noContent().build();
    }
}
