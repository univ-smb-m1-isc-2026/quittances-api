package com.nobless.quittances.service;

import com.nobless.quittances.model.Propriete;
import com.nobless.quittances.repository.ProprieteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProprieteService {
    private final ProprieteRepository proprieteRepository;

    public ProprieteService(ProprieteRepository proprieteRepository) {
        this.proprieteRepository = proprieteRepository;
    }

    public Propriete create(Propriete propriete) {
        return proprieteRepository.save(propriete);
    }

    public List<Propriete> list() {
        return proprieteRepository.findAll();
    }

    public List<Propriete> listByIdProprios(Long idProprios) {
        return proprieteRepository.findByIdProprios(idProprios);
    }

    public void deleteById(Long id) {
        proprieteRepository.deleteById(id);
    }
}
