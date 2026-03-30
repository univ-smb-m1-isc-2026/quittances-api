package com.nobless.quittances.service;

import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.repository.ProprioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProprioService {
    private final ProprioRepository proprioRepository;

    public ProprioService(ProprioRepository proprioRepository) {
        this.proprioRepository = proprioRepository;
    }


    public Proprio create(Proprio p) {
        return proprioRepository.save(p);
    }

    public Proprio findByEmail(String email) {
        return proprioRepository.findByEmail(email);
    }

    public List<Proprio> list() {
        return proprioRepository.findAll();
    }

    public void deleteById(Long id) {
        proprioRepository.deleteById(id);
    }
}