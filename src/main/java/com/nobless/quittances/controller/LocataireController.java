package com.nobless.quittances.controller;

import com.nobless.quittances.controller.dto.ApiResponse;
import com.nobless.quittances.model.Locataire;
import com.nobless.quittances.service.LocataireService;
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

import java.util.List;

@RestController
public class LocataireController {

    private static final Logger log = LoggerFactory.getLogger(LocataireController.class);

    private final LocataireService LocataireService;

    public LocataireController(LocataireService LocataireService) {
        this.LocataireService = LocataireService;
    }

    @GetMapping("/api/locataires")
    public ApiResponse<List<Locataire>> listLocataires() {
        log.info("GET /api/proprios - listing proprietaires");
        List<Locataire> proprios = LocataireService.list();
        log.info("GET /api/proprios - {} proprietaires returned", proprios.size());
        if (proprios.isEmpty()) {
            return ApiResponse.info(proprios, "Aucun locataire en bdd");
        }
        return ApiResponse.success(proprios);
    }

    @PostMapping("/api/locataires")
    public ResponseEntity<ApiResponse<Locataire>> createLocataire(@RequestBody Locataire locataire) {
        Locataire createdLocataire = LocataireService.create(locataire);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdLocataire, "Locataire cree"));
    }

    @DeleteMapping("/api/locataires/{id}")
    public ApiResponse<Void> deleteLocataire(@PathVariable Long id) {
        LocataireService.deleteById(id);
        return ApiResponse.success(null, "Locataire supprime");
    }
}
