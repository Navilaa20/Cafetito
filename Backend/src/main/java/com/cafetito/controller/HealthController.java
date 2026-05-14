package com.cafetito.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/estado")
public class HealthController {

    // Cumple con FA0: Consultar estado del WS
    @GetMapping
    public ResponseEntity<Map<String, String>> consultarEstado() {
        Map<String, String> response = new HashMap<>();
        response.put("estado", "Activo");
        return ResponseEntity.ok(response);
    }
}