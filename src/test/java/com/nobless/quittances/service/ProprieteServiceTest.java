package com.nobless.quittances.service;

import com.nobless.quittances.model.Propriete;
import com.nobless.quittances.repository.LocataireRepository;
import com.nobless.quittances.repository.ProprieteRepository;
import com.nobless.quittances.repository.ProprioRepository;
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
import static org.mockito.Mockito.*;

class ProprieteServiceTest {

    @Mock
    private ProprieteRepository proprieteRepository;

    @Mock
    private ProprioRepository proprioRepository;

    @Mock
    private LocataireRepository locataireRepository;

    @InjectMocks
    private ProprieteService proprieteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_validPropriete_returnsSavedPropriete() {
        // Arrange
        Propriete p = new Propriete();
        p.setAdresse("10 rue des Lilas");
        p.setType("Studio");
        p.setIdProprio(1L);
        p.setIdLocataire(2L);
        p.setPeriodicite(1);

        when(proprieteRepository.findByAdresse("10 rue des Lilas")).thenReturn(Optional.empty());
        when(proprioRepository.existsById(1L)).thenReturn(true);
        when(locataireRepository.existsById(2L)).thenReturn(true);
        when(proprieteRepository.save(any(Propriete.class))).thenReturn(p);

        // Act
        Propriete result = proprieteService.create(p);

        // Assert
        assertNotNull(result);
        assertEquals("STUDIO", result.getType());
        verify(proprieteRepository, times(1)).save(p);
    }

    @Test
    void create_invalidType_throwsBadRequest() {
        // Arrange
        Propriete p = new Propriete();
        p.setType("MAISON");

        // Act & Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> proprieteService.create(p));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("type invalide"));
    }

    @Test
    void create_proprioNotFound_throwsBadRequest() {
        // Arrange
        Propriete p = new Propriete();
        p.setAdresse("11 rue des Lilas");
        p.setType("T2");
        p.setIdProprio(1L);
        p.setIdLocataire(2L);
        p.setPeriodicite(1);

        when(proprieteRepository.findByAdresse("11 rue des Lilas")).thenReturn(Optional.empty());
        when(proprioRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> proprieteService.create(p));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("proprietaire introuvable"));
    }

    @Test
    void list_returnsAllProprietes() {
        // Arrange
        when(proprieteRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Propriete> result = proprieteService.list();

        // Assert
        assertTrue(result.isEmpty());
        verify(proprieteRepository, times(1)).findAll();
    }

    @Test
    void listByIdProprio_returnsMatchingProprietes() {
        when(proprieteRepository.findByIdProprio(1L)).thenReturn(Collections.emptyList());

        List<Propriete> result = proprieteService.listByIdProprio(1L);

        assertNotNull(result);
        verify(proprieteRepository).findByIdProprio(1L);
    }

    @Test
    void updateById_existingPropriete_updatesAndSaves() {
        Propriete existing = new Propriete();
        existing.setId(10L);
        existing.setAdresse("10 rue de base");
        existing.setType("T2");

        Propriete payload = new Propriete();
        payload.setAdresse("14 rue Victor Hugo");
        payload.setType("duplex");
        payload.setIdProprio(1L);
        payload.setIdLocataire(2L);
        payload.setPeriodicite(3);

        when(proprieteRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(proprieteRepository.findByAdresse("14 rue Victor Hugo")).thenReturn(Optional.empty());
        when(proprioRepository.existsById(1L)).thenReturn(true);
        when(locataireRepository.existsById(2L)).thenReturn(true);
        when(proprieteRepository.save(existing)).thenReturn(existing);

        Propriete result = proprieteService.updateById(10L, payload);

        assertEquals("14 rue Victor Hugo", result.getAdresse());
        assertEquals("DUPLEX", result.getType());
        assertEquals(1L, result.getIdProprio());
        assertEquals(2L, result.getIdLocataire());
        assertEquals(3, result.getPeriodicite());
        verify(proprieteRepository).save(existing);
    }

    @Test
    void create_missingPeriodicite_throwsBadRequest() {
        Propriete p = new Propriete();
        p.setAdresse("12 rue des Lilas");
        p.setType("T2");
        p.setIdProprio(1L);
        p.setIdLocataire(2L);

        when(proprieteRepository.findByAdresse("12 rue des Lilas")).thenReturn(Optional.empty());
        when(proprioRepository.existsById(1L)).thenReturn(true);
        when(locataireRepository.existsById(2L)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> proprieteService.create(p));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("periodicite"));
    }

    @Test
    void updateById_notFound_throwsNotFound() {
        when(proprieteRepository.findById(404L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> proprieteService.updateById(404L, new Propriete()));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("propriete introuvable"));
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        // Act
        proprieteService.deleteById(1L);

        // Assert
        verify(proprieteRepository, times(1)).deleteById(1L);
    }

    @Test
    void create_duplicateAdresse_throwsConflict() {
        Propriete payload = new Propriete();
        payload.setAdresse("49 Allee des Bouquetins");
        payload.setType("T2");
        payload.setIdProprio(1L);
        payload.setIdLocataire(2L);
        payload.setPeriodicite(1);

        Propriete existing = new Propriete();
        existing.setId(99L);
        existing.setAdresse("49 Allee des Bouquetins");

        when(proprieteRepository.findByAdresse("49 Allee des Bouquetins")).thenReturn(Optional.of(existing));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> proprieteService.create(payload));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertTrue(ex.getReason().contains("adresse deja utilisee"));
        verify(proprieteRepository, never()).save(any(Propriete.class));
    }
}
