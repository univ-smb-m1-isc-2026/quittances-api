package com.nobless.quittances.service;

import com.nobless.quittances.model.Quittance;
import com.nobless.quittances.repository.QuittanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuittanceService {

    private final QuittanceRepository quittanceRepository;

    public QuittanceService(QuittanceRepository quittanceRepository) {
        this.quittanceRepository = quittanceRepository;
    }

    public List<Quittance> list() {
        return quittanceRepository.findAll();
    }

    public List<Quittance> listByIdProprio(Long idProprio) {
        return quittanceRepository.findByIdProprio(idProprio);
    }

    public Quittance create(Quittance quittance) {
        return quittanceRepository.save(quittance);
    }

    public Optional<Quittance> getById(Long id) {
        return quittanceRepository.findById(id);
    }

    public void delete(Long id) {
        quittanceRepository.deleteById(id);
    }
}
