package com.nobless.quittances.service;

import com.nobless.quittances.model.Admin;
import com.nobless.quittances.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_hashesPassword_returnsSavedAdmin() {
        Admin admin = new Admin();
        admin.setLogin("root");
        admin.setPassword("secret");

        when(passwordEncoder.encode("secret")).thenReturn("encodedSecret");
        when(adminRepository.save(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Admin result = adminService.create(admin);

        assertNotNull(result);
        assertEquals("encodedSecret", result.getPassword());
        verify(passwordEncoder, times(1)).encode("secret");
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void list_returnsAllAdmins() {
        when(adminRepository.findAll()).thenReturn(Collections.emptyList());

        List<Admin> result = adminService.list();

        assertTrue(result.isEmpty());
        verify(adminRepository).findAll();
    }

    @Test
    void updateById_existingAdmin_updatesAndHashesPassword() {
        Admin existing = new Admin();
        existing.setId(1L);
        existing.setLogin("old");
        existing.setPassword("oldPass");

        Admin payload = new Admin();
        payload.setLogin("new");
        payload.setPassword("newPass");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(adminRepository.save(existing)).thenReturn(existing);

        Admin result = adminService.updateById(1L, payload);

        assertEquals("new", result.getLogin());
        assertEquals("encodedNewPass", result.getPassword());
        verify(adminRepository).save(existing);
    }

    @Test
    void updateById_notFound_throwsNotFound() {
        when(adminRepository.findById(404L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> adminService.updateById(404L, new Admin()));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("admin introuvable"));
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        adminService.deleteById(1L);
        verify(adminRepository).deleteById(1L);
    }
}
