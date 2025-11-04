package com.example.eventmanagement.service;

import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.PackageEvent;
import com.example.eventmanagement.repository.PackageEventRepository;
import com.example.eventmanagement.repository.PackageRepository;

import com.example.eventmanagement.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PackageEventRepository packageEventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public List<PackageEntity> getAllPackages() {
        return packageRepository.findAll();
    }

    public Optional<PackageEntity> getPackageById(Integer id) {
        return packageRepository.findById(id);
    }

    public PackageEntity createPackages(PackageEntity p) {
        return packageRepository.save(p);
    }

    public PackageEntity updatePackage(Integer id, PackageEntity updated) {
        return packageRepository.findById(id)
                .map(p -> {
                    p.setNume(updated.getNume());
                    p.setLocatie(updated.getLocatie());
                    p.setDescriere(updated.getDescriere());
                    return packageRepository.save(p);
        }).orElseThrow(() -> new IllegalArgumentException("Pachetul nu exista"));
    }

    public void deletePackage(Integer id) {
        PackageEntity pachet = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pachetul nu exista"));

        packageEventRepository.deleteByPachet(pachet);
        packageRepository.delete(pachet);
    }

    public List<PackageEvent> getEventsForPackage(PackageEntity pachet) {
        return packageEventRepository.findByPachet(pachet);
    }

//    public Integer calculeazaLocuriDisponibile(PackageEntity pachet) {
//        List<PackageEvent> legaturi = packageEventRepository.findByPachet(pachet);
//        if (legaturi.isEmpty()) return 0;
//
//        return legaturi.stream()
//                .map(pe -> pe.getEveniment().getNumarLocuri())
//                .filter(num -> num != null)
//                .min(Integer::compareTo)
//                .orElse(0);
//    }

    public Integer calculeazaLocuriDisponibile(PackageEntity pachet) {
        int packageTicketsSold = ticketRepository.findByPachet(pachet).size();

        var legaturi = packageEventRepository.findByPachet(pachet);
        if (legaturi.isEmpty()) return 0;

        int limitPerPackage = legaturi.stream().mapToInt(pe -> {
            var ev = pe.getEveniment();

            int capacity = pe.getNumarLocuri() != null
                    ? pe.getNumarLocuri()
                    : (ev.getNumarLocuri() != null ? ev.getNumarLocuri() : 0);

            int eventTicketsSold = ticketRepository.findByEveniment(ev).size();

            int remainingForThisEvent = capacity - eventTicketsSold - packageTicketsSold;

            return Math.max(remainingForThisEvent, 0);
        }).min().orElse(0);

        return Math.max(limitPerPackage, 0);
    }

    public Page<PackageEntity> searchPackages(String name, String type, String eventName, Integer availableTickets, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<PackageEntity> allPackages = packageRepository.findAll();

        List<PackageEntity> filtered = allPackages.stream()
                .filter(p -> name == null || p.getNume().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> type == null || (p.getDescriere() != null && p.getDescriere().toLowerCase().contains(type.toLowerCase())))
                .filter(p -> {
                    if(availableTickets == null) return true;
                    Integer locuriDisponibile = calculeazaLocuriDisponibile(p);
                    return locuriDisponibile >= availableTickets;
                })
                .filter(p -> eventName == null || packageEventRepository.findByPachet(p).stream()
                        .anyMatch(pe -> pe.getEveniment().getNume().toLowerCase().contains(eventName.toLowerCase())))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());

        List<PackageEntity> paginated = start >= filtered.size() ? List.of() : filtered.subList(start, end);

        return new PageImpl<>(paginated, pageable, filtered.size());
    }

    public int countEventsInPackage(PackageEntity entity) {
        return packageEventRepository.findByPachet(entity).size();
    }
}
