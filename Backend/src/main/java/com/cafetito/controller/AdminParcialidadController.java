package com.cafetito.controller;

import com.cafetito.dto.ParcialidadDetalleResponseDTO;
import com.cafetito.service.ParcialidadAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/parcialidades")
public class AdminParcialidadController {

    private final ParcialidadAdminService service;

    public AdminParcialidadController(ParcialidadAdminService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<ParcialidadDetalleResponseDTO> obtenerDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerDetalle(id));
    }

    @PutMapping("/{id}/recibir")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<Void> recibirParcialidad(@PathVariable Long id) {
        service.recibirParcialidad(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<Void> rechazarParcialidad(@PathVariable Long id) {
        service.rechazarParcialidad(id);
        return ResponseEntity.ok().build();
    }
}
