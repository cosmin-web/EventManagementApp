package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.PackageEvent;
import com.example.eventmanagement.repository.PackageEventRepository;
import com.example.eventmanagement.service.EventService;
import com.example.eventmanagement.service.PackageEventService;
import com.example.eventmanagement.service.PackageService;
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

    private Map<String, Object> wrapWithLinks(Object data, Map<String, String> links) {
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
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista"));


        var list = packageEventService.getPackagesForEvent(event).stream()
                .map(pe -> wrapWithLinks(pe, eventToPackageLinks(eventId, pe.getPachet().getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/event-packets/{packetId}/events")
    public ResponseEntity<List<Map<String, Object>>> getEventsForPackage(@PathVariable Integer packetId) {
        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(() -> new IllegalArgumentException("Pachetul nu exista"));

        var list = packageEventService.getEventsForPackage(pachet).stream()
                .map(pe->wrapWithLinks(pe, packageToEventLinks(packetId, pe.getEveniment().getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/event-packets/{packetId}/events/{eventId}")
    public ResponseEntity<Map<String, Object>> createEventToPackage(
            @PathVariable Integer packetId,
            @PathVariable Integer eventId,
            @RequestParam(required = false) Integer numarLocuri)
    {
        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(()->  new IllegalArgumentException("Acest pachet nu exista"));

        Event eveniment = eventService.getEventById(eventId)
                .orElseThrow(()->new IllegalArgumentException("Acest eveniment nu exista"));

        PackageEvent relation = packageEventService.addEventToPackage(pachet, eveniment, numarLocuri);
        return ResponseEntity.ok(wrapWithLinks(relation, packageToEventLinks(packetId, eventId)));
    }

    @DeleteMapping("/event-packets/{packetId}/events/{eventId}")
    public ResponseEntity<Void> deleteEventFromPackage(@PathVariable Integer packetId, @PathVariable Integer eventId) {
        PackageEntity pachet = packageService.getPackageById(packetId)
                .orElseThrow(()-> new IllegalArgumentException("Acest pachet nu exista"));

        Event eveniment = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Acest eveniment nu exista"));

        packageEventService.removeEventFromPackage(pachet, eveniment);
        return ResponseEntity.noContent().build();
    }
}
