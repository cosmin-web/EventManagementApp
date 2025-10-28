package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.PackageEvent;
import com.example.eventmanagement.model.PackageEventId;
import com.example.eventmanagement.repository.PackageEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackageEventService {

    @Autowired
    private PackageEventRepository packageEventRepository;

    public List<PackageEvent> getAllRelations() {
        return packageEventRepository.findAll();
    }

    public Optional<PackageEvent> getRelation(PackageEntity pachet, Event eveniment) {
        return packageEventRepository.findById(new PackageEventId(pachet.getId(), eveniment.getId()));
    }

    public PackageEvent addEventToPackage(PackageEntity pachet, Event eveniment, Integer numarLocuri) {
        PackageEvent relation = new PackageEvent(pachet, eveniment, numarLocuri);
        return packageEventRepository.save(relation);
    }

    public void removeEventFromPackage(PackageEntity pachet, Event eveniment) {
        packageEventRepository.deleteByPachetAndEveniment(pachet, eveniment);
    }

    public List<PackageEvent> findByPackage(PackageEntity pachet) {
        return packageEventRepository.findByPachet(pachet);
    }

    public List<PackageEvent> findByEvent(Event eveniment) {
        return packageEventRepository.findByEvent(eveniment);
    }
}
