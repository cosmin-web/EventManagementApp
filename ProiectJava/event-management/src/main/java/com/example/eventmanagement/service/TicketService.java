package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.Ticket;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.repository.PackageRepository;
import com.example.eventmanagement.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PackageRepository packageRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketByCode(String code) {
        return ticketRepository.findById(code);
    }

    public Ticket createTicketForEvent(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista"));

        long bileteVandute = ticketRepository.findByEveniment(event).size();

        if (event.getNumarLocuri() == null || bileteVandute >= event.getNumarLocuri()) {
            throw new IllegalStateException("Nu mai sunt locuri disponibile pentru acest eveniment");
        }

        String cod = UUID.randomUUID().toString();
        Ticket ticket = new Ticket(cod, null, event);
        return ticketRepository.save(ticket);
    }

    public Ticket createTicketForPackage(Integer packageId) {
        PackageEntity pachet = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Pachetul nu exista."));

        String cod = UUID.randomUUID().toString();
        Ticket ticket = new Ticket(cod, pachet, null);
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(String cod) {
        if(!ticketRepository.existsByCod(cod)) {
            throw new IllegalArgumentException("Biletul nu exista");
        }
        ticketRepository.deleteById(cod);
    }

    public List<Ticket> getTicketsByEvent(Event event) {
        return ticketRepository.findByEveniment(event);
    }

    public List<Ticket> getTicketsByPackage(PackageEntity pachet) {
        return ticketRepository.findByPachet(pachet);
    }
}
