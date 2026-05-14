package com.cafetito.controller;


import com.cafetito.dto.AgricultorDetalleResponseDTO;
import com.cafetito.dto.AgricultorResponseDTO;
import com.cafetito.entity.Usuario;
import com.cafetito.service.AgricultorAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/agricultores")
public class AdminAgricultorController {

    private final AgricultorAdminService service;

    public AdminAgricultorController(AgricultorAdminService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<List<AgricultorResponseDTO>> listarAgricultores(
            @RequestParam(value = "nit", required = false) String nit) {
        // El service ya gestiona si viene un NIT para filtrar o si devuelve todos
        List<AgricultorResponseDTO> respuesta = service.listarAgricultores(nit);
        return ResponseEntity.ok(respuesta);
    }

    // ✅ CUMPLE Paso 9: Este es el endpoint que Angular va a consumir
    @GetMapping("/{nit}")
    @PreAuthorize("hasRole('BENEFICIO')") // Asegúrate de tener tu seguridad configurada
    public ResponseEntity<AgricultorDetalleResponseDTO> obtenerDetalle(@PathVariable String nit) {
        return ResponseEntity.ok(service.obtenerDetalle(nit));
    }
}
