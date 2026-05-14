package com.cafetito.controller;

import com.cafetito.dto.ActualizarEstadoTransportistaRequestDTO;
import com.cafetito.dto.TransportistaResponseDTO;
import com.cafetito.service.TransportistaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/transportistas")
public class AdminTransportistaController {

    private final TransportistaService service;

    public AdminTransportistaController(TransportistaService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<List<TransportistaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<List<TransportistaResponseDTO>> buscar(
            @RequestParam(value = "cui", required = false) String cui,
            @RequestParam(value = "nit", required = false) String nit) {

        // Si mandan el CUI
        if (cui != null && !cui.trim().isEmpty()) {
            return ResponseEntity.ok(service.buscarPorCui(cui));
        }

        // Si mandan el NIT (que ahora internamente lo tratamos como idUsuario)
        if (nit != null && !nit.trim().isEmpty()) {
            // ✅ Convertimos el String a Long para que el Service no se queje
            Long idUsuario = Long.parseLong(nit);
            return ResponseEntity.ok(service.listarPorAgricultor(idUsuario));
        }

        // Si no mandan ni CUI ni NIT, devuelve todos
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<List<TransportistaResponseDTO>> filtrarPorEstado(@RequestParam(value = "activo", required = false) Boolean activo) {
        return ResponseEntity.ok(service.filtrarPorEstado(activo));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<TransportistaResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoTransportistaRequestDTO dto) {
        TransportistaResponseDTO updated = service.actualizarEstado(id, dto);
        return ResponseEntity.ok(updated);
    }

}