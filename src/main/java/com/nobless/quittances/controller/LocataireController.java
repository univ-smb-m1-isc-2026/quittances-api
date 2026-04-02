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
import org.springframework.web.bind.annotation.PutMapping;
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
        log.info("GET /api/locataires - listing locataires");
        List<Locataire> locataires = LocataireService.list();
        log.info("GET /api/locataires - {} locataires returned", locataires.size());
        if (locataires.isEmpty()) {
            ApiResponse<List<Locataire>> response = ApiResponse.info(locataires, "Aucun locataire en bdd");
            log.info("GET /api/locataires - response state={}", response.getState());
            return response;
        }
        ApiResponse<List<Locataire>> response = ApiResponse.success(locataires);
        log.info("GET /api/locataires - response state={}", response.getState());
        return response;
    }

    @PostMapping("/api/locataires")
    public ResponseEntity<ApiResponse<Locataire>> createLocataire(@RequestBody Locataire locataire) {
        log.info("POST /api/locataires - create locataire request");
        Locataire createdLocataire = LocataireService.create(locataire);
        ApiResponse<Locataire> response = ApiResponse.success(createdLocataire, "Locataire cree");
        log.info("POST /api/locataires - created locataire id={}, state={}", createdLocataire.getId(), response.getState());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/api/locataires/{id}")
    public ApiResponse<Locataire> updateLocataire(@PathVariable Long id, @RequestBody Locataire locataire) {
        log.info("PUT /api/locataires/{} - update locataire request", id);
        Locataire updatedLocataire = LocataireService.updateById(id, locataire);
        ApiResponse<Locataire> response = ApiResponse.success(updatedLocataire, "Locataire modifie");
        log.info("PUT /api/locataires/{} - response state={}", id, response.getState());
        return response;
    }

    @DeleteMapping("/api/locataires/{id}")
    public ApiResponse<Void> deleteLocataire(@PathVariable Long id) {
        log.info("DELETE /api/locataires/{} - delete locataire request", id);
        LocataireService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Locataire supprime");
        log.info("DELETE /api/locataires/{} - response state={}", id, response.getState());
        return response;
    }
}
