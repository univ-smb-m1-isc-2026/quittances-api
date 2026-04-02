package com.nobless.quittances.service;

import com.nobless.quittances.model.Locataire;
import com.nobless.quittances.repository.LocataireRepository;
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

class LocataireServiceTest {

    @Mock
    private LocataireRepository locataireRepository;

    @InjectMocks
    private LocataireService locataireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_validLocataire_returnsSavedLocataire() {
        // Arrange
        Locataire l = new Locataire();
        l.setPrenom("John");
        l.setNom("Doe");

        when(locataireRepository.save(any(Locataire.class))).thenReturn(l);

        // Act
        Locataire result = locataireService.create(l);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getPrenom());
        verify(locataireRepository, times(1)).save(l);
    }

    @Test
    void list_returnsAllLocataires() {
        // Arrange
        when(locataireRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Locataire> result = locataireService.list();

        // Assert
        assertTrue(result.isEmpty());
        verify(locataireRepository, times(1)).findAll();
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        // Act
        locataireService.deleteById(1L);

        // Assert
        verify(locataireRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateById_existingLocataire_updatesAndReturns() {
        Locataire existing = new Locataire();
        existing.setId(1L);
        existing.setNom("Old");

        Locataire payload = new Locataire();
        payload.setNom("New");
        payload.setPrenom("Name");

        when(locataireRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(locataireRepository.save(existing)).thenReturn(existing);

        Locataire result = locataireService.updateById(1L, payload);

        assertEquals("New", result.getNom());
        assertEquals("Name", result.getPrenom());
        verify(locataireRepository).findById(1L);
        verify(locataireRepository).save(existing);
    }

    @Test
    void updateById_notFound_throwsNotFound() {
        when(locataireRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> locataireService.updateById(999L, new Locataire()));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("locataire introuvable"));
    }
}
