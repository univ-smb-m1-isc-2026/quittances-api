package com.nobless.quittances.service;

import com.nobless.quittances.model.Locataire;
import com.nobless.quittances.model.Propriete;
import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.model.Quittance;
import com.nobless.quittances.repository.LocataireRepository;
import com.nobless.quittances.repository.ProprieteRepository;
import com.nobless.quittances.repository.ProprioRepository;
import com.nobless.quittances.repository.QuittanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class QuittanceService {

    private static final String DEFAULT_STATUT = "ENVOYEE";
    private static final DateTimeFormatter FRENCH_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final QuittanceRepository quittanceRepository;
    private final ProprioRepository proprioRepository;
    private final LocataireRepository locataireRepository;
    private final ProprieteRepository proprieteRepository;

    public QuittanceService(
            QuittanceRepository quittanceRepository,
            ProprioRepository proprioRepository,
            LocataireRepository locataireRepository,
            ProprieteRepository proprieteRepository
    ) {
        this.quittanceRepository = quittanceRepository;
        this.proprioRepository = proprioRepository;
        this.locataireRepository = locataireRepository;
        this.proprieteRepository = proprieteRepository;
    }

    public List<Quittance> list() {
        return quittanceRepository.findAll();
    }

    public List<Quittance> listByProprioId(Long proprioId) {
        return quittanceRepository.findByProprioId(proprioId);
    }

    public Quittance create(Quittance quittance) {
        Long proprioId = extractRequiredId(quittance.getProprio() == null ? null : quittance.getProprio().getId(), "proprio.id");
        Long locataireId = extractRequiredId(quittance.getLocataire() == null ? null : quittance.getLocataire().getId(), "locataire.id");
        Long proprieteId = extractRequiredId(quittance.getPropriete() == null ? null : quittance.getPropriete().getId(), "propriete.id");

        Proprio proprio = proprioRepository.findById(proprioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "proprio introuvable"));
        Locataire locataire = locataireRepository.findById(locataireId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "locataire introuvable"));
        Propriete propriete = proprieteRepository.findById(proprieteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "propriete introuvable"));

        if (propriete.getIdProprio() == null || !propriete.getIdProprio().equals(proprio.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "propriete non rattachee a ce proprietaire");
        }
        if (propriete.getIdLocataire() == null || !propriete.getIdLocataire().equals(locataire.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "propriete non rattachee a ce locataire");
        }

        String normalizedPeriod = normalizeRequiredText(quittance.getPeriod(), "period");
        if (quittanceRepository.existsByProprieteIdAndPeriod(propriete.getId(), normalizedPeriod)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "quittance deja existante pour cette periode");
        }

        quittance.setProprio(proprio);
        quittance.setLocataire(locataire);
        quittance.setPropriete(propriete);
        quittance.setPeriod(normalizedPeriod);
        quittance.setPaymentDate(normalizeOptionalText(quittance.getPaymentDate(), currentDateFr()));
        quittance.setSignatureCity(normalizeOptionalText(quittance.getSignatureCity(), propriete.getVille()));
        quittance.setSignatureImage(normalizeOptionalText(quittance.getSignatureImage(), null));
        quittance.setStatut(normalizeStatut(quittance.getStatut()));

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
            Long proprioId = extractRequiredId(payload.getProprio().getId(), "proprio.id");
            Proprio proprio = proprioRepository.findById(proprioId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "proprio introuvable"));
            existing.setProprio(proprio);
        }
        if (payload.getLocataire() != null) {
            Long locataireId = extractRequiredId(payload.getLocataire().getId(), "locataire.id");
            Locataire locataire = locataireRepository.findById(locataireId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "locataire introuvable"));
            existing.setLocataire(locataire);
        }
        if (payload.getPropriete() != null) {
            Long proprieteId = extractRequiredId(payload.getPropriete().getId(), "propriete.id");
            Propriete propriete = proprieteRepository.findById(proprieteId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "propriete introuvable"));
            existing.setPropriete(propriete);
        }
        if (payload.getPeriod() != null) {
            existing.setPeriod(normalizeRequiredText(payload.getPeriod(), "period"));
        }
        if (payload.getPaymentDate() != null) {
            existing.setPaymentDate(normalizeOptionalText(payload.getPaymentDate(), existing.getPaymentDate()));
        }
        if (payload.getSignatureCity() != null) {
            existing.setSignatureCity(normalizeOptionalText(payload.getSignatureCity(), existing.getSignatureCity()));
        }
        if (payload.getSignatureImage() != null) {
            existing.setSignatureImage(normalizeOptionalText(payload.getSignatureImage(), existing.getSignatureImage()));
        }
        if (payload.getStatut() != null) {
            existing.setStatut(normalizeStatut(payload.getStatut()));
        }

        if (existing.getPropriete() != null && existing.getPropriete().getId() != null && existing.getPeriod() != null) {
            Optional<Quittance> duplicate = quittanceRepository.findByProprieteIdAndPeriod(
                    existing.getPropriete().getId(),
                    existing.getPeriod()
            );
            if (duplicate.isPresent() && !duplicate.get().getId().equals(existing.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "quittance deja existante pour cette periode");
            }
        }

        return quittanceRepository.save(existing);
    }

    private Long extractRequiredId(Long id, String fieldName) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " requis");
        }
        return id;
    }

    private String normalizeRequiredText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " requis");
        }
        return normalizePeriodLabel(value.trim());
    }

    private String normalizeOptionalText(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? fallback : trimmed;
    }

    private String normalizeStatut(String rawStatut) {
        if (rawStatut == null || rawStatut.trim().isEmpty()) {
            return DEFAULT_STATUT;
        }

        String normalized = rawStatut
                .trim()
                .toUpperCase(Locale.ROOT)
            .replace(' ', '_');

        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "");

        return switch (normalized) {
            case "ENVOYEE", "ENVOYE", "SENT" -> "ENVOYEE";
            case "EN_ATTENTE", "ATTENTE", "PENDING" -> "EN_ATTENTE";
            case "PAYEE", "PAYE", "PAID" -> "PAYEE";
            case "ECHEC", "FAILED", "FAIL" -> "ECHEC";
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "statut invalide");
        };
    }

    private String currentDateFr() {
        return LocalDate.now().format(FRENCH_DATE_FORMAT);
    }

    private String normalizePeriodLabel(String value) {
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "");
        return normalized;
    }
}
