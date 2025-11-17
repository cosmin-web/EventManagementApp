package com.example.eventmanagement.controller;

import com.example.clientservice.dto.PublicClientDTO;
import com.example.eventmanagement.dto.EventDTO;
import com.example.eventmanagement.integration.ClientApiClient;
import com.example.eventmanagement.mapper.EventMapper;
import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.UserEntity;
import com.example.eventmanagement.service.EventService;
import com.example.eventmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-manager/events")
@Tag(name = "Events", description = "Operatii pentru gestionarea evenimentelor")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientApiClient clientApiClient;

    private Map<String, Object> wrap(Object data, Map<String, String> links) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", data);
        response.put("links", links);
        return response;
    }

    private Map<String, String> eventLinks(Integer id) {
        return Map.of(
          "self", "/api/event-manager/events/" + id,
          "parent", "/api/event-manager/events"
        );
    }

    private EventDTO enrichEvent(EventEntity event) {
        EventDTO dto = EventMapper.fromEntity(event);

        int soldOnEvent = eventService.countTicketsSold(event);
        int packageImpact = eventService.countPackageTicketsImpactForEvent(event);

        int capacity = event.getNumarLocuri() != null ? event.getNumarLocuri() : 0;
        int available = capacity - soldOnEvent - packageImpact;

        dto.setTicketsSold(soldOnEvent);
        dto.setAvailableTickets(Math.max(available, 0));

        if (event.getOwner() != null)
            dto.setOwnerEmail(event.getOwner().getEmail());

        return dto;
    }

    @Operation(summary = "Listare evenimente", description = "Returneaza o lista paginata de evenimente.")
    @ApiResponse(responseCode = "200", description = "Lista de evenimente a fost returnata.")
    @ApiResponse(responseCode = "400", description = "Parametri de filtrare invalizi.")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, name = "available_tickets") Integer availableTickets,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5", name = "items_per_page") int size) {

        var resultPage = eventService.searchEvents(name, location, availableTickets, page, size);

        var data = resultPage.getContent().stream()
                .map(event -> wrap(enrichEvent(event), eventLinks(event.getId())))
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", data);
        response.put("currentPage", resultPage.getNumber());
        response.put("totalItems", resultPage.getTotalElements());
        response.put("totalPages", resultPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtine un eveniment dupa ID")
    @ApiResponse(responseCode = "200", description = "Evenimentul a fost gasit.")
    @ApiResponse(responseCode = "404", description = "Evenimentul nu a fost gasit.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id)
                .map(event -> ResponseEntity.ok(wrap(enrichEvent(event), eventLinks(event.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creeaza un eveniment nou")
    @ApiResponse(responseCode = "201", description = "Evenimentul a fost creat.")
    @ApiResponse(responseCode = "400", description = "Date invalide sau lipsa campuri obligatorii.")
    @ApiResponse(responseCode = "404", description = "Proprietarul nu a fost gasit.")
    @ApiResponse(responseCode = "409", description = "Exista un eveniment cu acest nume.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody @Valid EventDTO dto) {
        UserEntity owner = userService.getUserById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Proprietarul nu exista."));

        EventEntity event = EventMapper.toEntity(dto, owner);
        EventEntity saved = eventService.createEvent(event);
        EventDTO response = enrichEvent(saved);

        return ResponseEntity.created(URI.create("/api/event-manager/events/" + saved.getId()))
                .body(wrap(response, eventLinks(saved.getId())));
    }

    @Operation(summary = "Actualizeaza un eveniment existent")
    @ApiResponse(responseCode = "200", description = "Evenimentul a fost actualizat.")
    @ApiResponse(responseCode = "400", description = "Date invalide.")
    @ApiResponse(responseCode = "404", description = "Evenimentul nu a fost gasit.")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEvent(@PathVariable Integer id,
                                                           @RequestBody @Valid EventDTO dto) {
        UserEntity owner = userService.getUserById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Proprietarul nu exista."));

        EventEntity entity = EventMapper.toEntity(dto, owner);
        EventEntity updated = eventService.updateEvent(id, entity);
        EventDTO response = enrichEvent(updated);

        return ResponseEntity.ok(wrap(response, eventLinks(updated.getId())));
    }

    @Operation(summary = "Sterge un eveniment după ID")
    @ApiResponse(responseCode = "204", description = "Evenimentul a fost sters.")
    @ApiResponse(responseCode = "404", description = "Evenimentul nu a fost gasit.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}/clients")
    public ResponseEntity<List<PublicClientDTO>> getClientsForEvent(
            @PathVariable Integer eventId,
            @RequestParam Integer ownerId) {

        var event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista."));

        if (!event.getOwner().getId().equals(ownerId)) {
            return ResponseEntity.status(403).build();
        }

        List<PublicClientDTO> clients = clientApiClient.getClientsByEvent(eventId);
        return ResponseEntity.ok(clients);
    }

}
