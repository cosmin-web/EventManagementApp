package com.example.eventservice.repository;

import com.example.eventservice.model.EventEntity;
import com.example.eventservice.model.PackageEntity;
import com.example.eventservice.model.PackageEvent;
import com.example.eventservice.model.PackageEventIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageEventRepository extends JpaRepository<PackageEvent, PackageEventIdEntity>
{
    List<PackageEvent> findByPachet(PackageEntity pachet);
    List<PackageEvent> findByEveniment(EventEntity eveniment);
    void deleteByPachet(PackageEntity pachet);
    void deleteByEveniment(EventEntity eveniment);
    void deleteByPachetAndEveniment(PackageEntity pachet, EventEntity eveniment);
}
