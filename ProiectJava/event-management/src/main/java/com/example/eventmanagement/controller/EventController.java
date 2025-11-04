package com.example.eventmanagement.controller;

import com.example.eventmanagement.dto.EventDTO;
import com.example.eventmanagement.mapper.EventMapper;
import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.UserEntity;
import com.example.eventmanagement.service.EventService;
import com.example.eventmanagement.service.UserService;
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
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

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

//    @GetMapping
//    public ResponseEntity<List<Map<String, Object>>> getAllEvents() {
//        var list = eventService.getAllEvents().stream()
//                .map(EventMapper::fromEntity)
//                .map(dto -> wrap(dto, eventLinks(dto.getId())))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(list);
//    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, name = "available_tickets") Integer availableTickets,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5", name = "items_per_page") int size) {

        var resultPage = eventService.searchEvents(name, location, availableTickets, page, size);

        var data = resultPage.getContent().stream()
                .map(EventMapper::fromEntity)
                .map(dto -> wrap(dto, eventLinks(dto.getId())))
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", data);
        response.put("currentPage", resultPage.getNumber());
        response.put("totalItems", resultPage.getTotalElements());
        response.put("totalPages", resultPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id)
                .map(EventMapper::fromEntity)
                .map(dto -> ResponseEntity.ok(wrap(dto, eventLinks(dto.getId()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody @Valid EventDTO dto) {
        UserEntity owner = userService.getUserById(dto.getOwnerId())
                .orElseThrow(()-> new IllegalArgumentException("Proprietarul nu exista"));

        EventEntity event = EventMapper.toEntity(dto, owner);
        EventEntity saved = eventService.createEvent(event);
        EventDTO response = EventMapper.fromEntity(saved);

        return ResponseEntity.created(URI.create("/api/event-manager/events/" + saved.getId()))
                .body(wrap(response, eventLinks(saved.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDTO dto) {
        UserEntity owner = userService.getUserById(dto.getOwnerId())
                .orElseThrow(()-> new IllegalArgumentException("Proprietarul nu exista"));

        EventEntity entity = EventMapper.toEntity(dto, owner);
        EventEntity updated = eventService.updateEvent(id, entity);
        EventDTO response = EventMapper.fromEntity(updated);

        return ResponseEntity.ok(wrap(response, eventLinks(updated.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
