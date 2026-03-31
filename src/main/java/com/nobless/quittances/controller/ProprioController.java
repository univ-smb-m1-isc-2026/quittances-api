package com.nobless.quittances.controller;

import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.service.ProprioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nobless.quittances.controller.dto.LoginRequest;
import com.nobless.quittances.controller.dto.JwtResponse;
import com.nobless.quittances.security.JwtUtil;

import java.util.List;

@RestController
public class ProprioController {

    private final ProprioService proprioService;

    private final JwtUtil jwtUtil;

    public ProprioController(ProprioService proprioService, JwtUtil jwtUtil) {
        this.proprioService = proprioService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/api/proprios")
    public List<Proprio> listProprios() {
        List<Proprio> proprios = proprioService.list();
        return proprios;
    }

    @PostMapping("/api/proprios")
    public Proprio createProprio(@RequestBody Proprio proprio) {
        return proprioService.create(proprio);
    }

    @DeleteMapping("/api/proprios/{id}")
    public void deleteProprio(@PathVariable Long id) {
        proprioService.deleteById(id);
    }

    @PostMapping("/api/proprios/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("Tentative de connexion pour email: '{}'", loginRequest.getEmail());
        Proprio proprio = proprioService.findByEmail(loginRequest.getEmail());
        log.info("Résultat findByEmail('{}') = {}", loginRequest.getEmail(), proprio);
        if (proprio == null) {
            log.warn("Aucun proprio trouvé pour email: '{}'", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
        }
        if (!proprioService.passwordMatches(loginRequest.getPassword(), proprio.getPassword())) {
            log.warn("Mot de passe incorrect pour email: '{}'", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
        }
        String jwt = jwtUtil.generateToken(proprio);
        log.info("Connexion réussie pour email: '{}'", loginRequest.getEmail());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
