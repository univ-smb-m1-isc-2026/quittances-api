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
        p.setType("Studio");
        p.setIdProprio(1L);
        p.setIdLocataire(2L);

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
        p.setType("T2");
        p.setIdProprio(1L);
        p.setIdLocataire(2L);

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
    void deleteById_callsRepositoryDelete() {
        // Act
        proprieteService.deleteById(1L);

        // Assert
        verify(proprieteRepository, times(1)).deleteById(1L);
    }
}
