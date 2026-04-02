package com.nobless.quittances.service;

import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.repository.ProprioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProprioServiceTest {

    @Mock
    private ProprioRepository proprioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProprioService proprioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_hashesPassword_returnsSavedProprio() {
        // Arrange
        Proprio p = new Proprio();
        p.setPassword("myPassword123");
        p.setEmail("test@example.com");

        when(passwordEncoder.encode("myPassword123")).thenReturn("encodedPassword");
        when(proprioRepository.save(any(Proprio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Proprio result = proprioService.create(p);

        // Assert
        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder, times(1)).encode("myPassword123");
        verify(proprioRepository, times(1)).save(p);
    }

    @Test
    void passwordMatches_callsPasswordEncoder() {
        // Arrange
        when(passwordEncoder.matches("raw", "encoded")).thenReturn(true);

        // Act
        boolean matches = proprioService.passwordMatches("raw", "encoded");

        // Assert
        assertTrue(matches);
        verify(passwordEncoder, times(1)).matches("raw", "encoded");
    }

    @Test
    void findByEmail_delegatesToRepository() {
        // Arrange
        Proprio p = new Proprio();
        when(proprioRepository.findByEmail("test@example.com")).thenReturn(p);

        // Act
        Proprio result = proprioService.findByEmail("test@example.com");

        // Assert
        assertEquals(p, result);
        verify(proprioRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void list_returnsAllProprios() {
        // Arrange
        when(proprioRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Proprio> result = proprioService.list();

        // Assert
        assertTrue(result.isEmpty());
        verify(proprioRepository, times(1)).findAll();
    }
}
