package com.nobless.quittances.controller;

import com.nobless.quittances.controller.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/api/health")
    public ApiResponse<Map<String, String>> health() {
        log.info("GET /api/health - health check request");
        ApiResponse<Map<String, String>> response = ApiResponse.info(
                Map.of("status", "UP", "message", "Le serveur backend tourne !"),
                "Service disponible"
        );
        log.info("GET /api/health - response state={}", response.getState());
        return response;
    }
}

