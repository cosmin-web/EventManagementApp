package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.service.EventService;
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
public class EventController {

    @Autowired
    private EventService eventService;

    private Map<String, Object> wrapWithLinks(Object data, Map<String, String> links) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("data", data);
        resp.put("links", links);
        return resp;
    }

    private Map<String, String> eventLinks(Integer id) {
        return Map.of(
          "self", "/api/event-manager/events/" + id,
          "parent", "/api/event-manager/events"
        );
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        var list = eventService.getAllEvents().stream()
                .map(e-> wrapWithLinks(e, eventLinks(e.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id)
                .map(e -> ResponseEntity.ok(wrapWithLinks(e, eventLinks(e.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody Event e) {
        Event saved = eventService.createEvent(e);
        return ResponseEntity.created(URI.create("/api/event-manager/events/" + saved.getId()))
                .body(wrapWithLinks(saved, eventLinks(saved.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEvent(@PathVariable Integer id, @RequestBody Event e) {
        Event updated = eventService.updateEvent(id, e);
        return ResponseEntity.ok(wrapWithLinks(updated, eventLinks(updated.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
