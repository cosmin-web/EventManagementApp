package com.example.eventservice.infrastructure.adapter.in.rest;

import com.example.eventservice.application.auth.AuthenticatedUser;
import com.example.eventservice.application.auth.AuthorizationService;

import com.example.eventservice.application.dto.EventDTO;
import com.example.eventservice.application.mapper.EventMapper;
import com.example.eventservice.domain.model.EventEntity;
import com.example.eventservice.domain.model.UserEntity;
import com.example.eventservice.application.service.EventService;
import com.example.eventservice.application.service.UserService;
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
    private AuthorizationService authorizationService;

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
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, name = "available_tickets") Integer availableTickets,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5", name = "items_per_page") int size) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.ADMIN,
                UserEntity.Role.OWNER_EVENT,
                UserEntity.Role.CLIENT
        );

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
    public ResponseEntity<Map<String, Object>> getEventById(
            @PathVariable Integer id,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.ADMIN,
                UserEntity.Role.OWNER_EVENT,
                UserEntity.Role.CLIENT
        );

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
    public ResponseEntity<Map<String, Object>> createEvent(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestBody @Valid EventDTO dto) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.OWNER_EVENT
        );

        Integer ownerId = current.getUserId();

        UserEntity owner = userService.getUserById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Proprietarul nu exista."));

        dto.setOwnerId(ownerId);

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
    public ResponseEntity<Map<String, Object>> updateEvent(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PathVariable Integer id,
            @RequestBody @Valid EventDTO dto) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.OWNER_EVENT
        );

        EventEntity existing = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista."));

        if (existing.getOwner() == null ||
                existing.getOwner().getId() == null ||
                !existing.getOwner().getId().equals(current.getUserId())) {
            return ResponseEntity.status(403).build();
        }

        UserEntity owner = existing.getOwner();
        dto.setOwnerId(owner.getId());

        EventEntity entity = EventMapper.toEntity(dto, owner);
        EventEntity updated = eventService.updateEvent(id, entity);
        EventDTO response = enrichEvent(updated);

        return ResponseEntity.ok(wrap(response, eventLinks(updated.getId())));
    }


    @Operation(summary = "Sterge un eveniment după ID")
    @ApiResponse(responseCode = "204", description = "Evenimentul a fost sters.")
    @ApiResponse(responseCode = "404", description = "Evenimentul nu a fost gasit.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PathVariable Integer id) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.OWNER_EVENT
        );

        EventEntity existing = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista."));

        if (existing.getOwner() == null ||
                existing.getOwner().getId() == null ||
                !existing.getOwner().getId().equals(current.getUserId())) {
            return ResponseEntity.status(403).build();
        }

        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}

