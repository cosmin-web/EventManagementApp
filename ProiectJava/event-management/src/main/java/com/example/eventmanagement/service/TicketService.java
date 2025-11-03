package com.example.eventmanagement.service;

import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.TicketEntity;
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

    public List<TicketEntity> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<TicketEntity> getTicketByCode(String code) {
        return ticketRepository.findById(code);
    }

    public TicketEntity createTicketForEvent(Integer eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evenimentul nu exista."));

        long bileteVandute = ticketRepository.findByEveniment(event).size();

        if (event.getNumarLocuri() == null || bileteVandute >= event.getNumarLocuri()) {
            throw new IllegalStateException("Nu mai sunt locuri disponibile pentru acest eveniment.");
        }

        String cod = UUID.randomUUID().toString();
        TicketEntity ticket = new TicketEntity();
        ticket.setCod(cod);
        ticket.setEveniment(event);
        ticket.setPachet(null);

        return ticketRepository.save(ticket);
    }

    public TicketEntity createTicketForPackage(Integer packageId) {
        PackageEntity pachet = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Pachetul nu exista."));

        String cod = UUID.randomUUID().toString();
        TicketEntity ticket = new TicketEntity();
        ticket.setCod(cod);
        ticket.setPachet(pachet);
        ticket.setEveniment(null);

        return ticketRepository.save(ticket);
    }

    public void deleteTicket(String cod) {
        if (!ticketRepository.existsById(cod)) {
            throw new IllegalArgumentException("Biletul nu exista.");
        }
        ticketRepository.deleteById(cod);
    }

    public List<TicketEntity> getTicketsByEvent(EventEntity event) {
        return ticketRepository.findByEveniment(event);
    }

    public List<TicketEntity> getTicketsByPackage(PackageEntity pachet) {
        return ticketRepository.findByPachet(pachet);
    }
}
