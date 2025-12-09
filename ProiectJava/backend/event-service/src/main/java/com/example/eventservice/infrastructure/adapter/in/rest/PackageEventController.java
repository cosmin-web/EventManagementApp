package com.example.eventservice.infrastructure.adapter.in.rest;

import com.example.eventservice.application.auth.AuthenticatedUser;
import com.example.eventservice.application.dto.PackageEventDTO;
import com.example.eventservice.application.auth.AuthorizationService;
import com.example.eventservice.domain.model.EventEntity;
import com.example.eventservice.domain.model.PackageEntity;
import com.example.eventservice.domain.model.PackageEventEntity;
import com.example.eventservice.application.service.EventService;
import com.example.eventservice.application.service.PackageEventService;
import com.example.eventservice.application.service.PackageService;
import com.example.eventservice.domain.model.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-manager")
@Tag(name = "Package-Event Relations", description = "Operatii pentru gestionarea relatiilor dintre evenimente si pachete")
public class PackageEventController {

    @Autowired
    private PackageEventService packageEventService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private EventService eventService;

    @Autowired
    private AuthorizationService authorizationService;

    private Map<String, Object> wrap(Object data, Map<String, String> links) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("data", data);
        resp.put("links", links);
        return resp;
    }

    private Map<String, String> packageToEventLinks(Integer packetId, Integer eventId) {
        return Map.of(
                "self", "/api/event-manager/event-packets/" + packetId + "/events/" + eventId,
                "parent", "/api/event-manager/event-packets/" + packetId + "/events"
        );
    }

    private Map<String, String> eventToPackageLinks(Integer eventId, Integer packetId) {
        return Map.of(
                "self", "/api/event-manager/events/" + eventId + "/event-packets/" + packetId,
                "parent", "/api/event-manager/events/" + eventId + "/event-packets"
        );
    }

    private PackageEventDTO enrichRelation(PackageEventEntity relation) {
        PackageEventDTO dto = new PackageEventDTO();

        var pachet = relation.getPachet();
        var event = relation.getEveniment();

        dto.setPackageId(pachet.getId());
        dto.setEventId(event.getId());

        dto.setPackageName(pachet.getNume());
        dto.setEventName(event.getNume());
        dto.setEventLocation(event.getLocatie());

        return dto;
    }



    @Operation(summary = "Listare pachete pentru un eveniment", description = "Returneaza toate pachetele care includ un anumit eveniment.")
    @ApiResponse(responseCode = "200", description = "Lista a fost returnata.")
    @ApiResponse(responseCode = "404", description = "Evenimentul nu exista.")
    @GetMapping("/events/{eventId}/event-packets")
    public ResponseEntity<List<Map<String, Object>>> getPackagesForEvent(
            @PathVariable Integer eventId,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.ADMIN,
                UserEntity.Role.OWNER_EVENT,
                UserEntity.Role.CLIENT
        );

        EventEntity event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista"));

        var list = packageEventService.getPackagesForEvent(event).stream()
                .map(this::enrichRelation)
                .map(dto -> wrap(dto, eventToPackageLinks(dto.getEventId(), dto.getPackageId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Listare evenimente dintr-un pachet", description = "Returneaza toate evenimentele asociate unui pachet.")
    @ApiResponse(responseCode = "200", description = "Lista a fost returnata.")
    @ApiResponse(responseCode = "404", description = "Pachetul nu exista.")
    @GetMapping("/event-packets/{packetId}/events")
    public ResponseEntity<List<Map<String, Object>>> getEventsForPackage(
            @PathVariable Integer packetId,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.ADMIN,
                UserEntity.Role.OWNER_EVENT,
                UserEntity.Role.CLIENT
        );

        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(() -> new IllegalArgumentException("Pachetul nu exista"));

        var list = packageEventService.getEventsForPackage(pachet).stream()
                .map(this::enrichRelation)
                .map(dto -> wrap(dto, packageToEventLinks(dto.getPackageId(), dto.getEventId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Asociaza un eveniment unui pachet")
    @ApiResponse(responseCode = "200", description = "Evenimentul a fost adaugat in pachet.")
    @ApiResponse(responseCode = "400", description = "Date invalide.")
    @ApiResponse(responseCode = "404", description = "Evenimentul sau pachetul nu a fost gasit.")
    @ApiResponse(responseCode = "409", description = "Evenimentul este deja asociat cu acest pachet.")
    @PostMapping("/event-packets/{packetId}/events/{eventId}")
    public ResponseEntity<Map<String, Object>> createEventToPackage(
            @PathVariable Integer packetId,
            @PathVariable Integer eventId,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestBody(required = false) PackageEventDTO body) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.OWNER_EVENT
        );

        Integer ownerId = current.getUserId();

        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(() -> new IllegalArgumentException("Acest pachet nu exista."));

        EventEntity eveniment = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Acest eveniment nu exista."));

        if (pachet.getOwner() == null || pachet.getOwner().getId() == null ||
                eveniment.getOwner() == null || eveniment.getOwner().getId() == null ||
                !pachet.getOwner().getId().equals(ownerId) ||
                !eveniment.getOwner().getId().equals(ownerId)) {
            return ResponseEntity.status(403).build();
        }

        if (packageEventService.getRelation(pachet, eveniment).isPresent()) {
            throw new IllegalArgumentException("Evenimentul este deja asociat cu acest pachet.");
        }

        PackageEventEntity relation = packageEventService.addEventToPackage(pachet, eveniment);
        PackageEventDTO dto = enrichRelation(relation);

        return ResponseEntity.ok(
                wrap(dto, packageToEventLinks(dto.getPackageId(), dto.getEventId()))
        );
    }


    @Operation(summary = "Sterge un eveniment dintr-un pachet")
    @ApiResponse(responseCode = "204", description = "Relatia a fost stearsa cu succes.")
    @ApiResponse(responseCode = "404", description = "Evenimentul sau pachetul nu a fost gasit.")
    @DeleteMapping("/event-packets/{packetId}/events/{eventId}")
    public ResponseEntity<Void> deleteEventFromPackage(
            @PathVariable Integer packetId,
            @PathVariable Integer eventId,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        AuthenticatedUser current = authorizationService.requireUser(
                authorizationHeader,
                UserEntity.Role.OWNER_EVENT
        );

        Integer ownerId = current.getUserId();

        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(() -> new IllegalArgumentException("Acest pachet nu exista"));

        EventEntity eveniment = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Acest eveniment nu exista"));

        if (pachet.getOwner() == null || pachet.getOwner().getId() == null ||
                eveniment.getOwner() == null || eveniment.getOwner().getId() == null ||
                !pachet.getOwner().getId().equals(ownerId) ||
                !eveniment.getOwner().getId().equals(ownerId)) {
            return ResponseEntity.status(403).build();
        }

        packageEventService.removeEventFromPackage(pachet, eveniment);
        return ResponseEntity.noContent().build();
    }
}
