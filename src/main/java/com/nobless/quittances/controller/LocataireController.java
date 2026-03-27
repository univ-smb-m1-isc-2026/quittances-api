package com.nobless.quittances.controller;

import com.nobless.quittances.model.Locataire;
import com.nobless.quittances.service.LocataireService;
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
public class LocataireController {

    private static final Logger log = LoggerFactory.getLogger(LocataireController.class);

    private final LocataireService LocataireService;

    public LocataireController(LocataireService LocataireService) {
        this.LocataireService = LocataireService;
    }

    @GetMapping("/api/locataires")
    public List<Locataire> listLocataires() {
        log.info("GET /api/proprios - listing proprietaires");
        List<Locataire> proprios = LocataireService.list();
        log.info("GET /api/proprios - {} proprietaires returned", proprios.size());
        return proprios;
    }

    @PostMapping("/api/locataires")
    public Locataire createLocataire(@RequestBody Locataire locataire) {
        return LocataireService.create(locataire);
    }

    @DeleteMapping("/api/locataires/{id}")
    public void deleteLocataire(@PathVariable Long id) {
        LocataireService.deleteById(id);
    }
}
