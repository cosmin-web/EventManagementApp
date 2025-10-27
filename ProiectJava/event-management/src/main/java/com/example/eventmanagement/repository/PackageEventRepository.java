package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.PackageEvent;
import com.example.eventmanagement.model.PackageEventId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageEventRepository extends JpaRepository<PackageEvent, PackageEventId>
{
    List<PackageEvent> findByPachet(Package pachet);
    List<PackageEvent> findByEvent(Event eveniment);
    List<PackageEvent> findByNumarLocuri(Integer numarLocuri);
    long deleteByPachet(Package pachet);
    long deleteByEveniment(Event eveniment);
    long deleteByPachetAndEveniment(Package pachet, Event eveniment);
}
