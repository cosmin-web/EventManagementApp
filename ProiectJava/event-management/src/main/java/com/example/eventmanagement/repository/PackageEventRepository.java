package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.PackageEvent;
import com.example.eventmanagement.model.PackageEventId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageEventRepository extends JpaRepository<PackageEvent, PackageEventId>
{
    List<PackageEvent> findByPachet(PackageEntity pachet);
    List<PackageEvent> findByEvent(Event eveniment);
    List<PackageEvent> findByNumarLocuri(Integer numarLocuri);
    long deleteByPachet(PackageEntity pachet);
    long deleteByEveniment(Event eveniment);
    long deleteByPachetAndEveniment(PackageEntity pachet, Event eveniment);
}
