package com.nobless.quittances.service;

import com.nobless.quittances.model.Quittance;
import com.nobless.quittances.repository.QuittanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuittanceService {

    private final QuittanceRepository quittanceRepository;

    public QuittanceService(QuittanceRepository quittanceRepository) {
        this.quittanceRepository = quittanceRepository;
    }

    public List<Quittance> list() {
        return quittanceRepository.findAll();
    }

    public List<Quittance> listByProprioId(Long proprioId) {
        return quittanceRepository.findByProprioId(proprioId);
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

    public Quittance updateById(Long id, Quittance payload) {
        Quittance existing = quittanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "quittance introuvable"));

        if (payload.getProprio() != null) {
            existing.setProprio(payload.getProprio());
        }
        if (payload.getLocataire() != null) {
            existing.setLocataire(payload.getLocataire());
        }
        if (payload.getPropriete() != null) {
            existing.setPropriete(payload.getPropriete());
        }
        if (payload.getPeriod() != null) {
            existing.setPeriod(payload.getPeriod());
        }
        if (payload.getPaymentDate() != null) {
            existing.setPaymentDate(payload.getPaymentDate());
        }
        if (payload.getSignatureCity() != null) {
            existing.setSignatureCity(payload.getSignatureCity());
        }
        if (payload.getSignatureImage() != null) {
            existing.setSignatureImage(payload.getSignatureImage());
        }

        return quittanceRepository.save(existing);
    }
}
