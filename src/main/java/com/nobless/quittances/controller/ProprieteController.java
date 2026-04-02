package com.nobless.quittances.controller;

import com.nobless.quittances.controller.dto.ApiResponse;
import com.nobless.quittances.model.Propriete;
import com.nobless.quittances.service.ProprieteService;
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
public class ProprieteController {

    private static final Logger log = LoggerFactory.getLogger(ProprieteController.class);

    private final ProprieteService proprieteService;

    public ProprieteController(ProprieteService proprieteService) {
        this.proprieteService = proprieteService;
    }

    @GetMapping("/api/proprietes")
    public ApiResponse<List<Propriete>> listProprietes() {
        log.info("GET /api/proprietes - listing proprietes");
        List<Propriete> proprietes = proprieteService.list();
        log.info("GET /api/proprietes - {} proprietes returned", proprietes.size());
        if (proprietes.isEmpty()) {
            return ApiResponse.info(proprietes, "Aucune propriete en bdd");
        }
        return ApiResponse.success(proprietes);
    }

    @GetMapping("/api/proprietes/{id_proprios}")
    public ApiResponse<List<Propriete>> listProprietesByProprio(@PathVariable("id_proprios") Long idProprios) {
        log.info("GET /api/proprietes/{} - listing proprietes by proprio", idProprios);
        List<Propriete> proprietes = proprieteService.listByIdProprio(idProprios);
        log.info("GET /api/proprietes/{} - {} proprietes returned", idProprios, proprietes.size());
        if (proprietes.isEmpty()) {
            return ApiResponse.info(proprietes, "Aucune propriete en bdd");
        }
        return ApiResponse.success(proprietes);
    }

    @PostMapping("/api/proprietes")
    public ResponseEntity<ApiResponse<Propriete>> createPropriete(@RequestBody Propriete propriete) {
        log.info("POST /api/proprietes - create propriete request");
        Propriete createdPropriete = proprieteService.create(propriete);
        ApiResponse<Propriete> response = ApiResponse.success(createdPropriete, "Propriete creee");
        log.info("POST /api/proprietes - created propriete id={}, state={}", createdPropriete.getId(), response.getState());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/api/proprietes/{id}")
    public ApiResponse<Propriete> updatePropriete(@PathVariable Long id, @RequestBody Propriete propriete) {
        log.info("PUT /api/proprietes/{} - update propriete request", id);
        Propriete updatedPropriete = proprieteService.updateById(id, propriete);
        ApiResponse<Propriete> response = ApiResponse.success(updatedPropriete, "Propriete modifiee");
        log.info("PUT /api/proprietes/{} - response state={}", id, response.getState());
        return response;
    }

    @DeleteMapping("/api/proprietes/{id}")
    public ApiResponse<Void> deletePropriete(@PathVariable Long id) {
        log.info("DELETE /api/proprietes/{} - delete propriete request", id);
        proprieteService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Propriete supprimee");
        log.info("DELETE /api/proprietes/{} - response state={}", id, response.getState());
        return response;
    }
}
