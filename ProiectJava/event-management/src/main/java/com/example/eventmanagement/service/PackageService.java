package com.example.eventmanagement.service;

import com.example.eventmanagement.model.PackageEntity;
import com.example.eventmanagement.model.PackageEvent;
import com.example.eventmanagement.repository.PackageEventRepository;
import com.example.eventmanagement.repository.PackageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PackageEventRepository packageEventRepository;

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

    public Integer calculeazaLocuriDisponibile(PackageEntity pachet) {
        List<PackageEvent> legaturi = packageEventRepository.findByPachet(pachet);
        if (legaturi.isEmpty()) return 0;

        return legaturi.stream()
                .map(pe -> pe.getEveniment().getNumarLocuri())
                .filter(num -> num != null)
                .min(Integer::compareTo)
                .orElse(0);
    }
}
