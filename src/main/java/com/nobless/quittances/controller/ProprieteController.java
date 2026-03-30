package com.nobless.quittances.controller;

import com.nobless.quittances.model.Propriete;
import com.nobless.quittances.service.ProprieteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public List<Propriete> listProprietes() {
        log.info("GET /api/proprietes - listing proprietes");
        List<Propriete> proprietes = proprieteService.list();
        log.info("GET /api/proprietes - {} proprietes returned", proprietes.size());
        return proprietes;
    }

    @GetMapping("/api/proprietes/{id_proprios}")
    public List<Propriete> listProprietesByProprio(@PathVariable("id_proprios") Long idProprios) {
        log.info("GET /api/proprietes/{} - listing proprietes by proprio", idProprios);
        List<Propriete> proprietes = proprieteService.listByIdProprios(idProprios);
        log.info("GET /api/proprietes/{} - {} proprietes returned", idProprios, proprietes.size());
        return proprietes;
    }

    @PostMapping("/api/proprietes")
    public Propriete createPropriete(@RequestBody Propriete propriete) {
        return proprieteService.create(propriete);
    }

    @DeleteMapping("/api/proprietes/{id}")
    public void deletePropriete(@PathVariable Long id) {
        proprieteService.deleteById(id);
    }
}
