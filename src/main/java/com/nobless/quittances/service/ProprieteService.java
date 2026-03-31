package com.nobless.quittances.service;

import com.nobless.quittances.model.Propriete;
import com.nobless.quittances.repository.LocataireRepository;
import com.nobless.quittances.repository.ProprieteRepository;
import com.nobless.quittances.repository.ProprioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class ProprieteService {
    private final ProprieteRepository proprieteRepository;
    private final ProprioRepository proprioRepository;
    private final LocataireRepository locataireRepository;

    public ProprieteService(
            ProprieteRepository proprieteRepository,
            ProprioRepository proprioRepository,
            LocataireRepository locataireRepository
    ) {
        this.proprieteRepository = proprieteRepository;
        this.proprioRepository = proprioRepository;
        this.locataireRepository = locataireRepository;
    }

    public Propriete create(Propriete propriete) {
        if (propriete.getIdProprio() == null || !proprioRepository.existsById(propriete.getIdProprio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idProprio invalide: proprietaire introuvable");
        }
        if (propriete.getIdLocataire() == null || !locataireRepository.existsById(propriete.getIdLocataire())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idLocataire invalide: locataire introuvable");
        }
        return proprieteRepository.save(propriete);
    }

    public List<Propriete> list() {
        return proprieteRepository.findAll();
    }

    public List<Propriete> listByIdProprio(Long idProprio) {
        return proprieteRepository.findByIdProprio(idProprio);
    }

    public void deleteById(Long id) {
        proprieteRepository.deleteById(id);
    }
}
