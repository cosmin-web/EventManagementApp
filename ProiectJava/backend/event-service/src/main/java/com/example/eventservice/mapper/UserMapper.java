package com.example.eventservice.mapper;

import com.example.eventservice.dto.UserDTO;
import com.example.eventservice.model.UserEntity;

public class UserMapper {

    public static UserDTO fromEntity(UserEntity entity) {
        if (entity == null) return null;

        return new UserDTO(
                entity.getId(),
                entity.getEmail(),
                entity.getRol().name()
        );
    }

    public static UserEntity toEntity(UserDTO dto, String parola) {
        if (dto == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setParola(parola != null ? parola : "defaultPassword");
        entity.setRol(UserEntity.Role.fromString(dto.getRol()));
        return entity;
    }
}
