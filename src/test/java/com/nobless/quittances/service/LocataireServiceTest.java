package com.nobless.quittances.service;

import com.nobless.quittances.model.Locataire;
import com.nobless.quittances.repository.LocataireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

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
}
