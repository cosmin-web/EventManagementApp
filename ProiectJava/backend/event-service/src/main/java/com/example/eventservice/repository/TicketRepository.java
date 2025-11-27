package com.example.eventservice.repository;

import com.example.eventservice.model.EventEntity;
import com.example.eventservice.model.PackageEntity;
import com.example.eventservice.model.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TicketRepository extends JpaRepository<TicketEntity, String>
{
    List<TicketEntity> findByPachet(PackageEntity pachet);
    List<TicketEntity> findByEveniment(EventEntity eveniment);
    boolean existsByCod(String cod);
}