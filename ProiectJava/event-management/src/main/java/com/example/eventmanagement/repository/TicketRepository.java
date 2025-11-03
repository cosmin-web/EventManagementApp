package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TicketRepository extends JpaRepository<TicketEntity, String>
{
    List<TicketEntity> findByPachet(PackageEntity pachet);
    List<TicketEntity> findByEveniment(EventEntity eveniment);
    boolean existsByCod(String cod);
}