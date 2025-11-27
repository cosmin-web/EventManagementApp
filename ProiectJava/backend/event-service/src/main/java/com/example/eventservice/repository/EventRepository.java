package com.example.eventservice.repository;

import com.example.eventservice.model.EventEntity;
import com.example.eventservice.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer>
{
    List<EventEntity> findByOwner(UserEntity owner);
    List<EventEntity> findByNumeContainingIgnoreCase(String nume);
    List<EventEntity> findByLocatieContainingIgnoreCase(String locatie);
    List<EventEntity> findByDescriereContainingIgnoreCase(String descriere);
}