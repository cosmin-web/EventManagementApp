package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Event;
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

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Integer id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        if(event.getNumarLocuri() == null || event.getNumarLocuri() <= 0) {
            throw new IllegalArgumentException("Numarul de locuri trebuie sa fie mai mare decat 0");
        }
        return eventRepository.save(event);
    }

    public Event updateEvent(Integer id, Event updatedEvent) {
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
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista."));

        packageEventRepository.deleteByEveniment(event);
        eventRepository.delete(event);
    }

    public List<PackageEvent> getPackagesForEvent(Event event) {
        return packageEventRepository.findByEvent(event);
    }
}
