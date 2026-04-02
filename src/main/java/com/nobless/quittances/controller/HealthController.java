package com.nobless.quittances.controller;

import com.nobless.quittances.controller.dto.ApiResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public ApiResponse<Map<String, String>> health() {
        return ApiResponse.info(
                Map.of("status", "UP", "message", "Le serveur backend tourne !"),
                "Service disponible"
        );
    }
}

