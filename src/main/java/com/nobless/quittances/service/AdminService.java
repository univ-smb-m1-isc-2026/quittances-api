package com.nobless.quittances.service;

import com.nobless.quittances.model.Admin;
import com.nobless.quittances.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Admin> list() {
        return adminRepository.findAll();
    }

    public Admin findByLogin(String login) {
        return adminRepository.findByLogin(login);
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return rawPassword != null
                && encodedPassword != null
                && passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Admin create(Admin admin) {
        if (admin.getPassword() != null && !admin.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        return adminRepository.save(admin);
    }

    public Admin updateById(Long id, Admin payload) {
        Admin existing = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "admin introuvable"));

        if (payload.getLogin() != null) {
            existing.setLogin(payload.getLogin());
        }

        if (payload.getPassword() != null && !payload.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(payload.getPassword()));
        }

        return adminRepository.save(existing);
    }

    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }
}
