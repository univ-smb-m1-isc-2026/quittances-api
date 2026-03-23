package com.nobless.quittances.controller;

import com.nobless.quittances.model.Proprio;
import com.nobless.quittances.service.ProprioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProprioController {

    private final ProprioService proprioService;

    public ProprioController(ProprioService proprioService) {
        this.proprioService = proprioService;
    }

    @GetMapping("/api/proprios")
    public List<Proprio> listProprios() {
        List<Proprio> proprios = proprioService.list();
        return proprios;
    }
}
