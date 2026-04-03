package com.nobless.quittances.controller;

import com.nobless.quittances.model.Admin;
import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.service.AdminService;
import com.nobless.quittances.service.ProprioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nobless.quittances.controller.dto.LoginRequest;
import com.nobless.quittances.controller.dto.JwtResponse;
import com.nobless.quittances.controller.dto.ApiResponse;
import com.nobless.quittances.security.JwtUtil;

import java.util.List;

@RestController
public class ProprioController {

    private static final Logger log = LoggerFactory.getLogger(ProprioController.class);

    private final ProprioService proprioService;

    private final AdminService adminService;

    private final JwtUtil jwtUtil;

    public ProprioController(ProprioService proprioService, AdminService adminService, JwtUtil jwtUtil) {
        this.proprioService = proprioService;
        this.adminService = adminService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/api/proprios")
    public ApiResponse<List<Proprio>> listProprios() {
        log.info("GET /api/proprios - listing proprios");
        List<Proprio> proprios = proprioService.list();
        if (proprios.isEmpty()) {
            ApiResponse<List<Proprio>> response = ApiResponse.info(proprios, "Aucun proprio en bdd");
            log.info("GET /api/proprios - response state={}", response.getState());
            return response;
        }
        ApiResponse<List<Proprio>> response = ApiResponse.success(proprios);
        log.info("GET /api/proprios - returned {} items, state={}", proprios.size(), response.getState());
        return response;
    }

    @PostMapping("/api/proprios")
    public ResponseEntity<ApiResponse<Proprio>> createProprio(@RequestBody Proprio proprio) {
        log.info("POST /api/proprios - create proprio request for email={}", proprio.getEmail());
        Proprio createdProprio = proprioService.create(proprio);
        ApiResponse<Proprio> response = ApiResponse.success(createdProprio, "Proprio cree");
        log.info("POST /api/proprios - created proprio id={}, state={}", createdProprio.getId(), response.getState());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/api/proprios/{id}")
    public ApiResponse<Proprio> updateProprio(@PathVariable Long id, @RequestBody Proprio proprio) {
        log.info("PUT /api/proprios/{} - update proprio request", id);
        Proprio updatedProprio = proprioService.updateById(id, proprio);
        ApiResponse<Proprio> response = ApiResponse.success(updatedProprio, "Proprio modifie");
        log.info("PUT /api/proprios/{} - response state={}", id, response.getState());
        return response;
    }

    @DeleteMapping("/api/proprios/{id}")
    public ApiResponse<Void> deleteProprio(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        log.info("DELETE /api/proprios/{} - delete proprio request (force={})", id, force);
        proprioService.deleteById(id, force);
        String message = force ? "Proprio et proprietes supprimes" : "Proprio supprime";
        ApiResponse<Void> response = ApiResponse.success(null, message);
        log.info("DELETE /api/proprios/{} - response state={}", id, response.getState());
        return response;
    }

    @PostMapping("/api/proprios/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody LoginRequest loginRequest) {
        log.info("Tentative de connexion pour email: '{}'", loginRequest.getEmail());

        if (loginRequest.getEmail() != null && loginRequest.getEmail().toLowerCase().endsWith("@root.com")) {
            Admin admin = adminService.findByLogin(loginRequest.getEmail());
            if (admin == null || !adminService.passwordMatches(loginRequest.getPassword(), admin.getPassword())) {
                log.warn("Echec de connexion admin pour login: '{}'", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Identifiants invalides"));
            }

            String adminJwt = jwtUtil.generateAdminToken(admin);
            log.info("Connexion admin reussie pour login: '{}'", loginRequest.getEmail());
            return ResponseEntity.ok(ApiResponse.success(new JwtResponse(adminJwt, true), "Connexion reussie"));
        }

        Proprio proprio = proprioService.findByEmail(loginRequest.getEmail());
        log.info("Résultat findByEmail('{}') = {}", loginRequest.getEmail(), proprio);
        if (proprio == null) {
            log.warn("Aucun proprio trouvé pour email: '{}'", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Identifiants invalides"));
        }
        if (!proprioService.passwordMatches(loginRequest.getPassword(), proprio.getPassword())) {
            log.warn("Mot de passe incorrect pour email: '{}'", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Identifiants invalides"));
        }
        String jwt = jwtUtil.generateToken(proprio);
        log.info("Connexion réussie pour email: '{}'", loginRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.success(new JwtResponse(jwt, false), "Connexion reussie"));
    }
}
