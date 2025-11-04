package com.example.eventmanagement.controller;

import com.example.eventmanagement.mapper.TicketMapper;
import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.TicketEntity;
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

//    @GetMapping("/tickets")
//    public ResponseEntity<List<Map<String, Object>>> getAllTickets() {
//        var list = ticketService.getAllTickets().stream()
//                .map(t -> wrap(t, ticketLinks(t.getCod())))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(list);
//    }

    @GetMapping("/tickets")
    public ResponseEntity<Map<String, Object>> getTickets(
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) String packageName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5", name = "items_per_page") int size) {

        var resultPage = ticketService.searchTickets(eventName, packageName, page, size);

        var data = resultPage.getContent().stream()
                .map(TicketMapper::fromEntity)
                .map(dto -> wrap(dto, ticketLinks(dto.getCod())))
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", data);
        response.put("currentPage", resultPage.getNumber());
        response.put("totalItems", resultPage.getTotalElements());
        response.put("totalPages", resultPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("tickets/{cod}")
    public ResponseEntity<Map<String, Object>> getTicketByCod(@PathVariable String cod) {
        return ticketService.getTicketByCode(cod)
                .map(t -> ResponseEntity.ok(wrap(t, ticketLinks(t.getCod()))))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Biletul nu exista")));
    }

    @GetMapping("/events/{eventId}/tickets")
    public ResponseEntity<List<Map<String, Object>>> getAllTicketsFromEvent(@PathVariable Integer eventId) {
        EventEntity eveniment = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista."));

        var list = ticketService.getTicketsByEvent(eveniment).stream()
                .map(t -> wrap(t, ticketLinks(t.getCod())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/event-packets/{packetId}/tickets")
    public ResponseEntity<List<Map<String, Object>>> getAllTicketsFromPackage(@PathVariable Integer packetId) {
        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(() -> new IllegalArgumentException("Acest pachet nu exista"));

        var evenimente = packageService.getEventsForPackage(pachet).stream()
                .map(pe -> Map.of(
                        "id", pe.getEveniment().getId(),
                        "nume", pe.getEveniment().getNume(),
                        "locatie", pe.getEveniment().getLocatie(),
                        "descriere", pe.getEveniment().getDescriere(),
                        "numarLocuri", pe.getNumarLocuri()
                ))
                .toList();

        var list = ticketService.getTicketsByPackage(pachet).stream()
                .map(t -> {
                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("cod", t.getCod());
                    data.put("pachet", Map.of(
                            "id", pachet.getId(),
                            "nume", pachet.getNume(),
                            "locatie", pachet.getLocatie(),
                            "descriere", pachet.getDescriere()
                    ));
                    data.put("evenimenteIncluse", evenimente);
                    return wrap(data, ticketLinks(t.getCod()));
                })
                .toList();

        return ResponseEntity.ok(list);
    }


    @PostMapping("/events/{eventId}/tickets")
    public ResponseEntity<Map<String, Object>> createTicketForEvent(@PathVariable Integer eventId) {
        TicketEntity ticket = ticketService.createTicketForEvent(eventId);
        return ResponseEntity.created(URI.create("/api/event-manager/tickets/" + ticket.getCod()))
                .body(wrap(ticket, ticketLinks(ticket.getCod())));
    }

    @PostMapping("/event-packets/{packetId}/tickets")
    public ResponseEntity<Map<String, Object>> createTicketForPackage(@PathVariable Integer packetId) {
        TicketEntity ticket = ticketService.createTicketForPackage(packetId);
        return ResponseEntity.created(URI.create("/api/event-manager/tickets/" + ticket.getCod()))
                .body(wrap(ticket, ticketLinks(ticket.getCod())));
    }

    @DeleteMapping("/tickets/{cod}")
    public ResponseEntity<Void> deleteTicket(@PathVariable String cod) {
        ticketService.deleteTicket(cod);
        return ResponseEntity.noContent().build();
    }
}
