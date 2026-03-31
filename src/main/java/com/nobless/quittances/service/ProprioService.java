package com.nobless.quittances.service;

import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.repository.ProprioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProprioService {
    private final ProprioRepository proprioRepository;
    private final PasswordEncoder passwordEncoder;

    public ProprioService(ProprioRepository proprioRepository, PasswordEncoder passwordEncoder) {
        this.proprioRepository = proprioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Proprio create(Proprio p) {
        if (p.getPassword() != null && !p.getPassword().isBlank()) {
            p.setPassword(passwordEncoder.encode(p.getPassword()));
        }
        return proprioRepository.save(p);
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return rawPassword != null
                && encodedPassword != null
                && passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Proprio findByEmail(String email) {
        return proprioRepository.findByEmail(email);
    }

    public List<Proprio> list() {
        return proprioRepository.findAll();
    }

    public void deleteById(Long id) {
        proprioRepository.deleteById(id);
    }
}