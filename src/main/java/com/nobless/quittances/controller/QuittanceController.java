package com.nobless.quittances.controller;

import com.nobless.quittances.controller.dto.ApiResponse;
import com.nobless.quittances.model.Quittance;
import com.nobless.quittances.service.QuittanceService;
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
public class QuittanceController {

    private static final Logger log = LoggerFactory.getLogger(QuittanceController.class);

    private final QuittanceService quittanceService;

    public QuittanceController(QuittanceService quittanceService) {
        this.quittanceService = quittanceService;
    }

    @GetMapping("/api/quittances")
    public ApiResponse<List<Quittance>> listQuittances() {
        log.info("GET /api/quittances - listing quittances");
        List<Quittance> quittances = quittanceService.list();
        log.info("GET /api/quittances - {} quittances returned", quittances.size());
        if (quittances.isEmpty()) {
            return ApiResponse.info(quittances, "Aucune quittance en bdd");
        }
        return ApiResponse.success(quittances);
    }

    @GetMapping("/api/quittances/{id_proprios}")
    public ApiResponse<List<Quittance>> listQuittancesByProprio(@PathVariable("id_proprios") Long idProprios) {
        log.info("GET /api/quittances/{} - listing quittances by proprio", idProprios);
        List<Quittance> quittances = quittanceService.listByIdProprio(idProprios);
        log.info("GET /api/quittances/{} - {} quittances returned", idProprios, quittances.size());
        if (quittances.isEmpty()) {
            return ApiResponse.info(quittances, "Aucune quittance en bdd");
        }
        return ApiResponse.success(quittances);
    }

    @PostMapping("/api/quittances")
    public ResponseEntity<ApiResponse<Quittance>> createQuittance(@RequestBody Quittance quittance) {
        log.info("POST /api/quittances - create new quittance");
        Quittance createdQuittance = quittanceService.create(quittance);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdQuittance, "Quittance creee"));
    }

    @DeleteMapping("/api/quittances/{id}")
    public ApiResponse<Void> deleteQuittance(@PathVariable Long id) {
        log.info("DELETE /api/quittances/{} - delete quittance", id);
        quittanceService.delete(id);
        return ApiResponse.success(null, "Quittance supprimee");
    }
}
