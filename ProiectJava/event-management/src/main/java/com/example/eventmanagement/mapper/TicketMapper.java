package com.example.eventmanagement.mapper;

import com.example.eventmanagement.dto.TicketDTO;
import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.TicketEntity;

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
