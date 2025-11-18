package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer>
{
    List<EventEntity> findByOwner(UserEntity owner);
    List<EventEntity> findByNumeContainingIgnoreCase(String nume);
    List<EventEntity> findByLocatieContainingIgnoreCase(String locatie);
    List<EventEntity> findByDescriereContainingIgnoreCase(String descriere);
}