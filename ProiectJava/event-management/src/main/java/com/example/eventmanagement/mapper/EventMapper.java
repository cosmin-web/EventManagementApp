package com.example.eventmanagement.mapper;

import com.example.eventmanagement.dto.EventDTO;
import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.UserEntity;

public class EventMapper {

    public static EventDTO fromEntity(EventEntity entity) {
        if(entity == null) return null;

        return new EventDTO(
                entity.getId(),
                entity.getNume(),
                entity.getLocatie(),
                entity.getDescriere(),
                entity.getNumarLocuri(),
                entity.getOwner() != null ? entity.getOwner().getId() : null
        );
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
