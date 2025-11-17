package com.example.clientservice.repository;

import com.example.clientservice.model.ClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.*;

public interface ClientRepository extends MongoRepository<ClientDocument, String> {
    //metode automate
    //repo.findAll();
    //repo.findById(email);
    //repo.save(client);
    //repo.deleteById(email);

    Optional<ClientDocument> findByEmail(String email);

    @Query("{ 'nume': { $regex: ?0, $options: 'i' } }")
    List<ClientDocument> findByNumeLike(String regex);

    @Query("{ 'prenume': { $regex: ?0, $options: 'i' } }")
    List<ClientDocument> findByPrenumeLike(String regex);
}