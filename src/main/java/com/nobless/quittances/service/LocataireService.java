package com.nobless.quittances.service;

import com.nobless.quittances.model.Locataire;
import com.nobless.quittances.repository.LocataireRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocataireService {
    private final LocataireRepository LocataireRepository;

    public LocataireService(LocataireRepository LocataireRepository) {
        this.LocataireRepository = LocataireRepository;
    }

    public Locataire create(Locataire l) {
        return LocataireRepository.save(l);
    }

    public List<Locataire> list() {
        return LocataireRepository.findAll();
    }

    public void deleteById(Long id) {
        LocataireRepository.deleteById(id);
    }
}

