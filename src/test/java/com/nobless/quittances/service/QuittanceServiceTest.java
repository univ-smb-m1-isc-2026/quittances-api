package com.nobless.quittances.service;

import com.nobless.quittances.model.Locataire;
import com.nobless.quittances.model.Propriete;
import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.model.Quittance;
import com.nobless.quittances.repository.LocataireRepository;
import com.nobless.quittances.repository.ProprieteRepository;
import com.nobless.quittances.repository.ProprioRepository;
import com.nobless.quittances.repository.QuittanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class QuittanceServiceTest {

    @Mock
    private QuittanceRepository quittanceRepository;

    @Mock
    private ProprioRepository proprioRepository;

    @Mock
    private LocataireRepository locataireRepository;

    @Mock
    private ProprieteRepository proprieteRepository;

    @InjectMocks
    private QuittanceService quittanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void list_returnsAllQuittances() {
        // Arrange
        when(quittanceRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Quittance> result = quittanceService.list();

        // Assert
        assertTrue(result.isEmpty());
        verify(quittanceRepository, times(1)).findAll();
    }

    @Test
    void listByProprioId_delegatesToRepository() {
        // Arrange
        when(quittanceRepository.findByProprioId(1L)).thenReturn(Collections.emptyList());

        // Act
        List<Quittance> result = quittanceService.listByProprioId(1L);

        // Assert
        assertTrue(result.isEmpty());
        verify(quittanceRepository, times(1)).findByProprioId(1L);
    }

    @Test
    void create_validQuittance_returnsSavedQuittance() {
        // Arrange
        Quittance q = new Quittance();
        Proprio proprio = new Proprio();
        proprio.setId(1L);
        Locataire locataire = new Locataire();
        locataire.setId(2L);
        Propriete propriete = new Propriete();
        propriete.setId(3L);
        propriete.setIdProprio(1L);
        propriete.setIdLocataire(2L);

        q.setProprio(proprio);
        q.setLocataire(locataire);
        q.setPropriete(propriete);
        q.setPeriod("avril 2026");

        when(proprioRepository.findById(1L)).thenReturn(Optional.of(proprio));
        when(locataireRepository.findById(2L)).thenReturn(Optional.of(locataire));
        when(proprieteRepository.findById(3L)).thenReturn(Optional.of(propriete));
        when(quittanceRepository.existsByProprieteIdAndPeriod(3L, "avril 2026")).thenReturn(false);
        when(quittanceRepository.save(any(Quittance.class))).thenReturn(q);

        // Act
        Quittance result = quittanceService.create(q);

        // Assert
        assertNotNull(result);
        assertEquals("ENVOYEE", result.getStatut());
        verify(quittanceRepository, times(1)).save(q);
    }

    @Test
    void getById_returnsOptional() {
        // Arrange
        Quittance q = new Quittance();
        when(quittanceRepository.findById(1L)).thenReturn(Optional.of(q));

        // Act
        Optional<Quittance> result = quittanceService.getById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(q, result.get());
        verify(quittanceRepository, times(1)).findById(1L);
    }

    @Test
    void delete_callsRepositoryDelete() {
        // Act
        quittanceService.delete(1L);

        // Assert
        verify(quittanceRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateById_existingQuittance_updatesAndReturns() {
        Quittance existing = new Quittance();
        existing.setId(5L);
        existing.setPeriod("mars 2026");
        Propriete propriete = new Propriete();
        propriete.setId(10L);
        existing.setPropriete(propriete);

        Quittance payload = new Quittance();
        payload.setPeriod("avril 2026");
        payload.setSignatureCity("Paris");

        when(quittanceRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(quittanceRepository.findByProprieteIdAndPeriod(10L, "avril 2026")).thenReturn(Optional.empty());
        when(quittanceRepository.save(existing)).thenReturn(existing);

        Quittance result = quittanceService.updateById(5L, payload);

        assertEquals("avril 2026", result.getPeriod());
        assertEquals("Paris", result.getSignatureCity());
        verify(quittanceRepository).save(existing);
    }

    @Test
    void create_duplicatePeriodForSameProperty_throwsConflict() {
        Quittance q = new Quittance();
        Proprio proprio = new Proprio();
        proprio.setId(1L);
        Locataire locataire = new Locataire();
        locataire.setId(2L);
        Propriete propriete = new Propriete();
        propriete.setId(3L);
        propriete.setIdProprio(1L);
        propriete.setIdLocataire(2L);

        q.setProprio(proprio);
        q.setLocataire(locataire);
        q.setPropriete(propriete);
        q.setPeriod("avril 2026");

        when(proprioRepository.findById(1L)).thenReturn(Optional.of(proprio));
        when(locataireRepository.findById(2L)).thenReturn(Optional.of(locataire));
        when(proprieteRepository.findById(3L)).thenReturn(Optional.of(propriete));
        when(quittanceRepository.existsByProprieteIdAndPeriod(3L, "avril 2026")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> quittanceService.create(q));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(quittanceRepository, never()).save(any(Quittance.class));
    }

    @Test
    void updateById_notFound_throwsNotFound() {
        when(quittanceRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> quittanceService.updateById(999L, new Quittance()));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("quittance introuvable"));
    }
}
