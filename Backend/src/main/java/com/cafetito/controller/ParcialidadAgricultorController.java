package com.cafetito.controller;

import com.cafetito.dto.ParcialidadRequestDTO;
import com.cafetito.dto.ParcialidadResponseDTO;
import com.cafetito.service.ParcialidadAgricultorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parcialidades")
public class ParcialidadAgricultorController {

    private final ParcialidadAgricultorService service;

    public ParcialidadAgricultorController(ParcialidadAgricultorService service) {
        this.service = service;
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<ParcialidadResponseDTO> crear(@Valid @RequestBody ParcialidadRequestDTO dto) {
        // ✅ Extraemos el idUsuario (Long) en lugar del viejo nit
        Long idUsuario = getIdUsuarioFromAuth();
        ParcialidadResponseDTO response = service.crearParcialidad(dto, idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pesaje/{idPesaje}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<List<ParcialidadResponseDTO>> listarPorPesaje(@PathVariable Long idPesaje) {
        List<ParcialidadResponseDTO> list = service.listarPorPesaje(idPesaje);
        return ResponseEntity.ok(list);
    }

    // ✅ Nueva función para extraer el ID numérico del Token
    @SuppressWarnings("unchecked")
    private Long getIdUsuarioFromAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) auth.getDetails();
            Object idObj = details.get("idUsuario"); // Ajusta si el token lo llama distinto
            if (idObj != null) {
                if (idObj instanceof Number) return ((Number) idObj).longValue();
                else if (idObj instanceof String) return Long.parseLong((String) idObj);
            }
        }
        return null;
    }
}