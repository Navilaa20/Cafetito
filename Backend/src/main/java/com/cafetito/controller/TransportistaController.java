package com.cafetito.controller;

import com.cafetito.dto.TransportistaRequestDTO;
import com.cafetito.dto.TransportistaResponseDTO;
import com.cafetito.service.TransportistaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transportistas") // ✅ Mantenemos esta base para el agricultor
@CrossOrigin(origins = "*")
public class TransportistaController {

    private final TransportistaService service;

    public TransportistaController(TransportistaService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<TransportistaResponseDTO> crear(@Valid @RequestBody TransportistaRequestDTO dto) {
        Long idUsuario = getIdUsuarioFromAuth();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearTransportista(dto, idUsuario));
    }

    @GetMapping
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<List<TransportistaResponseDTO>> listar() {
        Long idUsuario = getIdUsuarioFromAuth();
        return ResponseEntity.ok(service.listarPorAgricultor(idUsuario));
    }

    // ✅ Este es el que usará el formulario de parcialidades
    @GetMapping("/disponibles")
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<List<TransportistaResponseDTO>> listarDisponibles() {
        Long idUsuario = getIdUsuarioFromAuth();
        return ResponseEntity.ok(service.listarDisponiblesPorAgricultor(idUsuario));
    }

    @SuppressWarnings("unchecked")
    private Long getIdUsuarioFromAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) auth.getDetails();
            Object idObj = details.get("idUsuario");
            if (idObj != null) {
                if (idObj instanceof Number) return ((Number) idObj).longValue();
                else if (idObj instanceof String) return Long.parseLong((String) idObj);
            }
        }
        return null;
    }
}