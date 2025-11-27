package com.example.eventservice.mapper;

import com.example.eventservice.dto.TicketDTO;
import com.example.eventservice.model.EventEntity;
import com.example.eventservice.model.PackageEntity;
import com.example.eventservice.model.TicketEntity;

public class TicketMapper {

    public static TicketDTO fromEntity(TicketEntity entity) {
        TicketDTO dto = new TicketDTO();
        dto.setCod(entity.getCod());
        dto.setEventId(entity.getEveniment() != null ? entity.getEveniment().getId() : null);
        dto.setPackageId(entity.getPachet() != null ? entity.getPachet().getId() : null);
        return dto;
    }

    public static TicketEntity toEntity(TicketDTO dto, PackageEntity pachet, EventEntity eveniment) {
        TicketEntity entity = new TicketEntity();
        entity.setCod(dto.getCod());
        entity.setPachet(pachet);
        entity.setEveniment(eveniment);
        return entity;
    }
}
