package com.example.eventservice.infrastructure.config;

import com.example.eventservice.domain.model.UserEntity;
import com.example.eventservice.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            System.out.println("Baza de date este goala. Se adauga utilizatorii initiali...");

            userRepository.save(new UserEntity(
                    null,
                    "admin@local",
                    "admin",
                    UserEntity.Role.ADMIN
            ));

            userRepository.save(new UserEntity(
                    null,
                    "owner@local",
                    "owner",
                    UserEntity.Role.OWNER_EVENT
            ));

            userRepository.save(new UserEntity(
                    null,
                    "client@local",
                    "client",
                    UserEntity.Role.CLIENT
            ));

            userRepository.save(new UserEntity(
                    null,
                    "clients_service",
                    "some_secret",
                    UserEntity.Role.SERVICE_CLIENT
            ));

            System.out.println("Utilizatorii default au fost creati cu succes!");
        } else {
            System.out.println("Utilizatorii exista deja în baza de date. Nu se adauga altii.");
        }
    }
}
