package com.example.eventmanagement.service;

import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.PackageEvent;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.repository.PackageEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PackageEventRepository packageEventRepository;

    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<EventEntity> getEventById(Integer id) {
        return eventRepository.findById(id);
    }

    public EventEntity createEvent(EventEntity event) {
        if(event.getNumarLocuri() == null || event.getNumarLocuri() <= 0) {
            throw new IllegalArgumentException("Numarul de locuri trebuie sa fie mai mare decat 0");
        }
        return eventRepository.save(event);
    }

    public EventEntity updateEvent(Integer id, EventEntity updatedEvent) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setNume(updatedEvent.getNume());
                    event.setLocatie(updatedEvent.getLocatie());
                    event.setDescriere(updatedEvent.getDescriere());
                    event.setNumarLocuri((updatedEvent.getNumarLocuri()));
                    return eventRepository.save(event);
                }).orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista"));
    }

    public void deleteEvent(Integer id) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista."));

        packageEventRepository.deleteByEveniment(event);
        eventRepository.delete(event);
    }

    public List<PackageEvent> getPackagesForEvent(EventEntity event) {
        return packageEventRepository.findByEveniment(event);
    }
}
