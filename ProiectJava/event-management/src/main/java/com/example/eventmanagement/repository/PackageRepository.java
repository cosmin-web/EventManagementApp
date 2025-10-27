package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Integer>
{
    List<Package> findByOwner(User owner);
    List<Package> findByNumeContainingIgnoreCase(String nume);
    List<Package> findByLocatieContainingIgnoreCase(String locatie);
    List<Package> findByDescriereContainingIgnoreCase(String descriere);
}