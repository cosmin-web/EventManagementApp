package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.EventEntity;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.PackageEvent;
import com.example.eventmanagement.model.PackageEventIdEntity;
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
