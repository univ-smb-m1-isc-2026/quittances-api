package com.nobless.quittances.service;

import com.nobless.quittances.model.Locataire;
import com.nobless.quittances.repository.LocataireRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Locataire updateById(Long id, Locataire payload) {
        Locataire existing = LocataireRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "locataire introuvable"));

        if (payload.getNom() != null) {
            existing.setNom(payload.getNom());
        }
        if (payload.getPrenom() != null) {
            existing.setPrenom(payload.getPrenom());
        }
        if (payload.getEmail() != null) {
            existing.setEmail(payload.getEmail());
        }
        if (payload.getTelephone() != null) {
            existing.setTelephone(payload.getTelephone());
        }

        return LocataireRepository.save(existing);
    }
}

