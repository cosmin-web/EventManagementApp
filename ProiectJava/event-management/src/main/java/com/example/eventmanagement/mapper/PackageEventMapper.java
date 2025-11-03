package com.example.eventmanagement.mapper;

import com.example.eventmanagement.dto.PackageEventDTO;
import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.PackageEvent;

public class PackageEventMapper {

    public static PackageEventDTO fromEntity(PackageEvent entity) {
        PackageEventDTO dto = new PackageEventDTO();
        dto.setEventId(entity.getEveniment().getId());
        dto.setPackageId(entity.getPachet().getId());
        dto.setNumarLocuri(entity.getNumarLocuri());
        return dto;
    }

    public static PackageEvent toEntity(PackageEventDTO dto, PackageEntity pachet, EventEntity eveniment) {
        PackageEvent pe = new PackageEvent();
        pe.setEveniment(eveniment);
        pe.setPachet(pachet);
        pe.setNumarLocuri(dto.getNumarLocuri());
        return pe;
    }
}
