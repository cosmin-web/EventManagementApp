package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.Ticket;
import com.example.eventmanagement.service.EventService;
import com.example.eventmanagement.service.PackageService;
import com.example.eventmanagement.service.TicketService;
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
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EventService eventService;

    @Autowired
    private PackageService packageService;

    private Map<String, Object> wrapWithLinks(Object data, Map<String, String> links) {
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

    @GetMapping("/tickets")
    public ResponseEntity<List<Map<String, Object>>> getAllTickets() {
        var list = ticketService.getAllTickets().stream()
                .map(t->wrapWithLinks(t, ticketLinks(t.getCod())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("tickets/{cod}")
    public ResponseEntity<Map<String, Object>> getTicketByCod(@PathVariable String cod) {
        Optional<Ticket> optional = ticketService.getTicketByCode(cod);
        if (optional.isPresent()) {
            Ticket t = optional.get();
            return ResponseEntity.ok(wrapWithLinks(t, ticketLinks(t.getCod())));
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Biletul nu exista"));
        }
    }

    @GetMapping("/events/{eventId}/tickets")
    public ResponseEntity<List<Map<String, Object>>> getAllTicketsFromEvent(@PathVariable Integer eventId) {
        Event eveniment = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Acest eveniment nu exista"));

        var list = ticketService.getTicketsByEvent(eveniment).stream()
                .map(t->wrapWithLinks(t, ticketLinks(t.getCod())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/event-packets/{packetId}/tickets")
    public ResponseEntity<List<Map<String, Object>>> getAllTicketsFromPackage(@PathVariable Integer packetId) {
        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(()-> new IllegalArgumentException("Acest pachet nu exista"));

        var list = ticketService.getTicketsByPackage(pachet).stream()
                .map(t->wrapWithLinks(t, ticketLinks(t.getCod())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/events/{eventId}/tickets")
    public ResponseEntity<Map<String, Object>> createTicketForEvent(@PathVariable Integer eventId) {
        Event eveniment = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Acest eveniment nu exista"));

        Ticket ticket = ticketService.createTicketForEvent(eveniment.getId());

        return ResponseEntity.created(URI.create("/api/event-manager/events/" + eventId + "/tickets"))
                .body(wrapWithLinks(ticket, ticketLinks(ticket.getCod())));
    }

    @PostMapping("/event-packets/{packetId}/tickets")
    public ResponseEntity<Map<String, Object>> createTicketForPackage(@PathVariable Integer packetId) {
        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(() -> new IllegalArgumentException("Acest pachet nu exista"));

        Ticket ticket = ticketService.createTicketForPackage(packetId);

        return ResponseEntity.created(URI.create("/api/event-manager/event-packets/" + packetId + "/tickets"))
                .body(wrapWithLinks(ticket, ticketLinks(ticket.getCod())));
    }

    @DeleteMapping("/tickets/{cod}")
    public ResponseEntity<Void> deleteTicket(@PathVariable String cod) {
        ticketService.deleteTicket(cod);
        return ResponseEntity.noContent().build();
    }
}
