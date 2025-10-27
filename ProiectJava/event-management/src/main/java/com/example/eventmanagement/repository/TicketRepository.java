package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TicketRepository extends JpaRepository<Ticket, String>
{
    List<Ticket> findByPachet(Package pachet);
    List<Ticket> findByEveniment(Event eveniment);
    boolean existsByCod(String cod);
}