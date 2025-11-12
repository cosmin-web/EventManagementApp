package com.example.clientservice.service;

import com.example.clientservice.model.ClientDocument;
import com.example.clientservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ClientService {

    private final ClientRepository repo;

    public ClientService(ClientRepository repo) {
        this.repo = repo;
    }

    public List<ClientDocument> findAlls(String nameLike) {
        if (!StringUtils.hasText(nameLike)) {
            return repo.findAll();
        }

        String regex = ".*" + nameLike + ".*";
        Set<ClientDocument> results = new LinkedHashSet<>();
        results.addAll(repo.findByNumeLike(regex));
        results.addAll(repo.findByPrenumeLike(regex));

        return new ArrayList<>(results);
    }

    public Optional<ClientDocument> findByEmail(String email) {
        return repo.findById(email);
    }

    public ClientDocument updateClient(ClientDocument document) {
        if(document.getEmail() == null || document.getEmail().isBlank()) {
            throw new IllegalArgumentException("Campul 'email' este obligatoriu.");
        }
        return repo.save(document);
    }

    public void delete(String email) {
        repo.deleteById(email);
    }
}
