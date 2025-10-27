package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer>
{
    List<Event> findByOwner(User owner);
    List<Event> findByNumeContainingIgnoreCase(String nume);
    List<Event> findByLocatieContainingIgnoreCase(String locatie);
    List<Event> findByDescriereContainingIgnoreCase(String descriere);
    List<Event> findByNumarLocuri(Integer numarLocuri);
}