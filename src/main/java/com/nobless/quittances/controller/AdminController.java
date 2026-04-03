package com.nobless.quittances.controller;

import com.nobless.quittances.controller.dto.ApiResponse;
import com.nobless.quittances.controller.dto.JwtResponse;
import com.nobless.quittances.controller.dto.LoginRequest;
import com.nobless.quittances.model.Admin;
import com.nobless.quittances.security.JwtUtil;
import com.nobless.quittances.service.AdminService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Pattern;

@RestController
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private static final Pattern ADMIN_LOGIN_PATTERN = Pattern.compile("^[a-zA-Z0-9]+@root\\.com$");

    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    public AdminController(AdminService adminService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/admins/login")
    public ResponseEntity<ApiResponse<JwtResponse>> loginAdmin(@RequestBody LoginRequest loginRequest) {
        String login = loginRequest.getEmail();
        log.info("POST /api/admins/login - tentative pour login={}", login);

        if (login == null || !ADMIN_LOGIN_PATTERN.matcher(login).matches()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Identifiants invalides"));
        }

        Admin admin = adminService.findByLogin(login);
        if (admin == null || !adminService.passwordMatches(loginRequest.getPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Identifiants invalides"));
        }

        String jwt = jwtUtil.generateAdminToken(admin);
        return ResponseEntity.ok(ApiResponse.success(new JwtResponse(jwt, true), "Connexion reussie"));
    }

    @GetMapping("/api/admins")
    public ApiResponse<List<Admin>> listAdmins() {
        log.info("GET /api/admins - listing admins");
        List<Admin> admins = adminService.list();
        if (admins.isEmpty()) {
            ApiResponse<List<Admin>> response = ApiResponse.info(admins, "Aucun admin en bdd");
            log.info("GET /api/admins - response state={}", response.getState());
            return response;
        }
        ApiResponse<List<Admin>> response = ApiResponse.success(admins);
        log.info("GET /api/admins - returned {} items, state={}", admins.size(), response.getState());
        return response;
    }

    @PostMapping("/api/admins")
    public ResponseEntity<ApiResponse<Admin>> createAdmin(@RequestBody Admin admin) {
        log.info("POST /api/admins - create admin request for login={}", admin.getLogin());
        Admin createdAdmin = adminService.create(admin);
        ApiResponse<Admin> response = ApiResponse.success(createdAdmin, "Admin cree");
        log.info("POST /api/admins - created admin id={}, state={}", createdAdmin.getId(), response.getState());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/api/admins/{id}")
    public ApiResponse<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        log.info("PUT /api/admins/{} - update admin request", id);
        Admin updatedAdmin = adminService.updateById(id, admin);
        ApiResponse<Admin> response = ApiResponse.success(updatedAdmin, "Admin modifie");
        log.info("PUT /api/admins/{} - response state={}", id, response.getState());
        return response;
    }

    @DeleteMapping("/api/admins/{id}")
    public ApiResponse<Void> deleteAdmin(@PathVariable Long id) {
        log.info("DELETE /api/admins/{} - delete admin request", id);
        adminService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Admin supprime");
        log.info("DELETE /api/admins/{} - response state={}", id, response.getState());
        return response;
    }
}
