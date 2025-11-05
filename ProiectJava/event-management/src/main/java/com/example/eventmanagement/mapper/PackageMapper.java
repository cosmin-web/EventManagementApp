package com.example.eventmanagement.mapper;

import com.example.eventmanagement.dto.PackageDTO;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.UserEntity;

public class PackageMapper {

    public static PackageDTO fromEntity(PackageEntity entity) {
        if(entity == null) return null;

        PackageDTO dto = new PackageDTO(
                entity.getId(),
                entity.getOwner().getId(),
                entity.getNume(),
                entity.getLocatie(),
                entity.getDescriere()
        );

        //adaugare noi campuri
        if(entity.getOwner() != null) {
            dto.setOwnerEmail(entity.getOwner().getEmail());
        }

        dto.setNumberOfEvents(0);
        dto.setAvailableTickets(0);

        return dto;
    }

    public static PackageEntity toEntity(PackageDTO dto, UserEntity owner) {
        PackageEntity entity= new PackageEntity();
        entity.setId(dto.getId());
        entity.setOwner(owner);
        entity.setNume(dto.getNume());
        entity.setLocatie(dto.getLocatie());
        entity.setDescriere(dto.getDescriere());
        return entity;
    }
}
