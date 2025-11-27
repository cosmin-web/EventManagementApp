package com.example.eventservice.mapper;

import com.example.eventservice.dto.EventDTO;
import com.example.eventservice.model.EventEntity;
import com.example.eventservice.model.UserEntity;

public class EventMapper {

    public static EventDTO fromEntity(EventEntity entity) {
        if (entity == null) return null;

        EventDTO dto = new EventDTO(
                entity.getId(),
                entity.getNume(),
                entity.getLocatie(),
                entity.getDescriere(),
                entity.getNumarLocuri(),
                entity.getOwner() != null ? entity.getOwner().getId() : null
        );

        //adaugare noi campuri
        if (entity.getOwner() != null)
            dto.setOwnerEmail(entity.getOwner().getEmail());

        dto.setTicketsSold(0);
        dto.setAvailableTickets(entity.getNumarLocuri());

        return dto;
    }

    public static EventEntity toEntity(EventDTO dto, UserEntity owner) {
        if (dto == null) return null;

        EventEntity e = new EventEntity();
        e.setId(dto.getId());
        e.setOwner(owner);
        e.setNume(dto.getNume());
        e.setLocatie(dto.getLocatie());
        e.setDescriere(dto.getDescriere());
        e.setNumarLocuri(dto.getNumarLocuri());
        return e;
    }
}
