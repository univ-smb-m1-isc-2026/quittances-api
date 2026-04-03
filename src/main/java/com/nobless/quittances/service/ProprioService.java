package com.nobless.quittances.service;

import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.model.Propriete;
import com.nobless.quittances.repository.ProprioRepository;
import com.nobless.quittances.repository.ProprieteRepository;
import com.nobless.quittances.repository.QuittanceRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProprioService {
    private final ProprioRepository proprioRepository;
    private final ProprieteRepository proprieteRepository;
    private final QuittanceRepository quittanceRepository;
    private final PasswordEncoder passwordEncoder;

    public ProprioService(
            ProprioRepository proprioRepository,
            ProprieteRepository proprieteRepository,
            QuittanceRepository quittanceRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.proprioRepository = proprioRepository;
        this.proprieteRepository = proprieteRepository;
        this.quittanceRepository = quittanceRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Proprio create(Proprio p) {
        validateRequiredFields(p.getNom(), p.getPrenom(), p.getEmail(), p.getTelephone());

        String normalizedEmail = p.getEmail().trim();
        String normalizedTelephone = p.getTelephone().trim();

        ensureEmailAvailable(normalizedEmail, null);
        ensureTelephoneAvailable(normalizedTelephone, null);

        p.setNom(p.getNom().trim());
        p.setPrenom(p.getPrenom().trim());
        p.setEmail(normalizedEmail);
        p.setTelephone(normalizedTelephone);

        if (p.getPassword() != null && !p.getPassword().isBlank()) {
            p.setPassword(passwordEncoder.encode(p.getPassword()));
        }
        return proprioRepository.save(p);
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return rawPassword != null
                && encodedPassword != null
                && passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Proprio findByEmail(String email) {
        return proprioRepository.findByEmail(email);
    }

    public List<Proprio> list() {
        return proprioRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id, boolean force) {
        try {
            if (force) {
                List<Propriete> properties = proprieteRepository.findByIdProprio(id);
                List<Long> propertyIds = properties.stream()
                        .map(Propriete::getId)
                        .filter(java.util.Objects::nonNull)
                        .toList();

                if (!propertyIds.isEmpty()) {
                    quittanceRepository.deleteByPropriete_IdIn(propertyIds);
                }

                quittanceRepository.deleteByProprioId(id);
                proprieteRepository.deleteByIdProprio(id);
                proprieteRepository.flush();
            }

            proprioRepository.deleteById(id);
            proprioRepository.flush();
        } catch (EmptyResultDataAccessException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "proprio introuvable");
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Suppression impossible: ce proprio est encore lie a d'autres donnees"
            );
        }
    }

    public Proprio updateById(Long id, Proprio payload) {
        Proprio existing = proprioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "proprio introuvable"));

        if (payload.getNom() != null) {
            existing.setNom(payload.getNom());
        }
        if (payload.getPrenom() != null) {
            existing.setPrenom(payload.getPrenom());
        }
        if (payload.getEmail() != null) {
            String email = payload.getEmail().trim();
            if (email.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email obligatoire");
            }
            ensureEmailAvailable(email, id);
            existing.setEmail(email);
        }
        if (payload.getTelephone() != null) {
            String telephone = payload.getTelephone().trim();
            validateTelephoneFormat(telephone);
            ensureTelephoneAvailable(telephone, id);
            existing.setTelephone(telephone);
        }
        if (payload.getPassword() != null && !payload.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(payload.getPassword()));
        }

        return proprioRepository.save(existing);
    }

    private void validateRequiredFields(String nom, String prenom, String email, String telephone) {
        if (nom == null || nom.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nom obligatoire");
        }
        if (prenom == null || prenom.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "prenom obligatoire");
        }
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email obligatoire");
        }
        if (telephone == null || telephone.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "telephone obligatoire");
        }

        validateTelephoneFormat(telephone.trim());
    }

    private void validateTelephoneFormat(String telephone) {
        if (telephone.length() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "telephone invalide (10 caracteres max)");
        }
    }

    private void ensureEmailAvailable(String email, Long currentId) {
        Proprio conflict = proprioRepository.findByEmail(email);
        if (conflict != null && (currentId == null || !conflict.getId().equals(currentId))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email deja utilise");
        }
    }

    private void ensureTelephoneAvailable(String telephone, Long currentId) {
        Proprio conflict = proprioRepository.findByTelephone(telephone);
        if (conflict != null && (currentId == null || !conflict.getId().equals(currentId))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "telephone deja utilise");
        }
    }
}