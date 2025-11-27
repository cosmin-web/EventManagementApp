package com.example.eventservice.domain.repository;

import com.example.eventservice.domain.model.EventEntity;
import com.example.eventservice.domain.model.PackageEntity;
import com.example.eventservice.domain.model.PackageEvent;
import com.example.eventservice.domain.model.PackageEventIdEntity;
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
