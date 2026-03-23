package com.nobless.quittances.controller;

import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.service.ProprioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProprioController {

    private static final Logger log = LoggerFactory.getLogger(ProprioController.class);

    private final ProprioService proprioService;

    public ProprioController(ProprioService proprioService) {
        this.proprioService = proprioService;
    }

    @GetMapping("/api/proprios")
    public List<Proprio> listProprios() {
        log.info("GET /api/proprios - listing proprietaires");
        List<Proprio> proprios = proprioService.list();
        log.info("GET /api/proprios - {} proprietaires returned", proprios.size());
        return proprios;
    }
}
