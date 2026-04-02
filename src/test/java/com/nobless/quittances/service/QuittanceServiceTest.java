package com.nobless.quittances.service;

import com.nobless.quittances.model.Quittance;
import com.nobless.quittances.repository.QuittanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuittanceServiceTest {

    @Mock
    private QuittanceRepository quittanceRepository;

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
        when(quittanceRepository.save(any(Quittance.class))).thenReturn(q);

        // Act
        Quittance result = quittanceService.create(q);

        // Assert
        assertNotNull(result);
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
}
