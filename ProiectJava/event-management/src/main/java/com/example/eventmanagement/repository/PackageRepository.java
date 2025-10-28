package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository<PackageEntity, Integer>
{
    List<PackageEntity> findByOwner(User owner);
    List<PackageEntity> findByNumeContainingIgnoreCase(String nume);
    List<PackageEntity> findByLocatieContainingIgnoreCase(String locatie);
    List<PackageEntity> findByDescriereContainingIgnoreCase(String descriere);
}