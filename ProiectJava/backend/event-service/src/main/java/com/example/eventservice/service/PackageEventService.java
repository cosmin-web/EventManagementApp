package com.example.eventservice.service;

import com.example.eventservice.model.EventEntity;
import com.example.eventservice.model.PackageEntity;
import com.example.eventservice.model.PackageEvent;
import com.example.eventservice.model.PackageEventIdEntity;
import com.example.eventservice.repository.PackageEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class PackageEventService {

    @Autowired
    private PackageEventRepository packageEventRepository;

    public List<PackageEvent> getAllRelations() {
        return packageEventRepository.findAll();
    }

    public Optional<PackageEvent> getRelation(PackageEntity pachet, EventEntity eveniment) {
        return packageEventRepository.findById(new PackageEventIdEntity(pachet.getId(), eveniment.getId()));
    }

    public List<PackageEvent> getPackagesForEvent(EventEntity eveniment) {
        return packageEventRepository.findByEveniment(eveniment);
    }

    public List<PackageEvent> getEventsForPackage(PackageEntity pachet) {
        return packageEventRepository.findByPachet(pachet);
    }

    public PackageEvent addEventToPackage(PackageEntity pachet, EventEntity eveniment) {
        PackageEvent relation = new PackageEvent(pachet, eveniment);
        return packageEventRepository.save(relation);
    }


    public void removeEventFromPackage(PackageEntity pachet, EventEntity eveniment) {
        packageEventRepository.deleteByPachetAndEveniment(pachet, eveniment);
    }
}
