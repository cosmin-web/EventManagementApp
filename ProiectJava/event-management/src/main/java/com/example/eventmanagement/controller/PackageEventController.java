package com.example.eventmanagement.controller;

import com.example.eventmanagement.dto.PackageEventDTO;
import com.example.eventmanagement.mapper.PackageEventMapper;
import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.PackageEvent;
import com.example.eventmanagement.service.EventService;
import com.example.eventmanagement.service.PackageEventService;
import com.example.eventmanagement.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-manager")
public class PackageEventController {

    @Autowired
    private PackageEventService packageEventService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private EventService eventService;

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
                "parent", "/api/event-manager/events/" + eventId + "event-packets"
        );
    }

    @GetMapping("/events/{eventId}/event-packets")
    public ResponseEntity<List<Map<String, Object>>> getPackagesForEvent(@PathVariable Integer eventId) {
        EventEntity event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista"));

        var list = packageEventService.getPackagesForEvent(event).stream()
                .map(PackageEventMapper::fromEntity)
                .map(dto -> wrap(dto, eventToPackageLinks(eventId, dto.getPackageId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/event-packets/{packetId}/events")
    public ResponseEntity<List<Map<String, Object>>> getEventsForPackage(@PathVariable Integer packetId) {
        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(() -> new IllegalArgumentException("Pachetul nu exista"));

        var list = packageEventService.getEventsForPackage(pachet).stream()
                .map(pe -> {
                    // extragem evenimentul asociat
                    EventEntity ev = pe.getEveniment();

                    // construim un obiect de tip Map pentru răspuns
                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("pachetId", pachet.getId());
                    data.put("eveniment", Map.of(
                            "id", ev.getId(),
                            "nume", ev.getNume(),
                            "locatie", ev.getLocatie(),
                            "descriere", ev.getDescriere()
                    ));
                    data.put("numarLocuri", pe.getNumarLocuri());

                    return wrap(data, packageToEventLinks(pachet.getId(), ev.getId()));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }


    @PostMapping("/event-packets/{packetId}/events/{eventId}")
    public ResponseEntity<Map<String, Object>> createEventToPackage(
            @PathVariable Integer packetId,
            @PathVariable Integer eventId,
            @RequestBody(required = false) PackageEventDTO dto) {

        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(()-> new IllegalArgumentException("Acest pachet nu exista"));

        EventEntity eveniment = eventService.getEventById(eventId)
                .orElseThrow(()-> new IllegalArgumentException("Acest eveniment nu exista"));

        Integer numarLocuri = dto != null ? dto.getNumarLocuri() : null;

        PackageEvent relation = packageEventService.addEventToPackage(pachet, eveniment, numarLocuri);
        return ResponseEntity.ok(wrap(relation, packageToEventLinks(packetId, eventId)));
    }

    @DeleteMapping("/event-packets/{packetId}/events/{eventId}")
    public ResponseEntity<Void> deleteEventFromPackage(@PathVariable Integer packetId, @PathVariable Integer eventId) {
        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(()->new IllegalArgumentException("Acest pachet nu exista"));

        EventEntity eveniment = eventService.getEventById(eventId)
                .orElseThrow(()->new IllegalArgumentException("Acest eveniment nu exista"));

        packageEventService.removeEventFromPackage(pachet, eveniment);
        return ResponseEntity.noContent().build();
    }
}
