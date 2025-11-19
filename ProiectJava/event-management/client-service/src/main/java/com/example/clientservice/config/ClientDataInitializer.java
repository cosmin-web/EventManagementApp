package com.example.clientservice.config;

import com.example.clientservice.model.ClientDocument;
import com.example.clientservice.repository.ClientRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientDataInitializer {

    @Autowired
    private ClientRepository repo;

    @PostConstruct
    public void init() {

        createIfMissing("demo1@local", "Ion", "Popescu");
        createIfMissing("demo2@local", "Maria", "Ionescu");

        System.out.println("ClientDataInitializer: verificare si inserare complete.");
    }

    private void createIfMissing(String email, String nume, String prenume) {
        if (repo.findByEmail(email).isEmpty()) {
            ClientDocument c = new ClientDocument();
            c.setEmail(email);
            c.setNume(nume);
            c.setPrenume(prenume);
            repo.save(c);
            System.out.println("Adăugat client demo: " + email);
        }
    }
}
