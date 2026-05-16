package com.cafetito.controller;

import com.cafetito.dto.ParcialidadResponseDTO;
import com.cafetito.dto.PesajeRequestDTO;
import com.cafetito.dto.PesajeResponseDTO;
import com.cafetito.entity.MedidaPeso;
import com.cafetito.service.PesajeAgricultorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pesajes")
public class PesajeAgricultorController {

    private final PesajeAgricultorService service;

    public PesajeAgricultorController(PesajeAgricultorService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<List<PesajeResponseDTO>> listar() {
        // Usamos el ID numérico
        Long idAgricultor = getIdAgricultorFromAuth();
        List<PesajeResponseDTO> list = service.listarPorAgricultor(idAgricultor);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<PesajeResponseDTO> crear(@RequestBody PesajeRequestDTO dto) {
        Long idAgricultor = getIdAgricultorFromAuth();
        return ResponseEntity.ok(service.crear(dto, idAgricultor));
    }

    @GetMapping("/{idPesaje}")
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<PesajeResponseDTO> obtener(@PathVariable Long idPesaje) {
        Long idAgricultor = getIdAgricultorFromAuth();
        PesajeResponseDTO dto = service.obtenerPorId(idPesaje, idAgricultor);
        return ResponseEntity.ok(dto);
    }

    @SuppressWarnings("unchecked")
    private Long getIdAgricultorFromAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) auth.getDetails();

            // ✅ CORRECCIÓN APLICADA: Usamos "idUsuario" para que coincida con el JwtAuthenticationFilter
            Object id = details.get("idUsuario");

            if (id instanceof Number) return ((Number) id).longValue();
            if (id instanceof String) return Long.valueOf((String) id);
        }
        // Fallback: Si no hay token (solo para pruebas locales), devolvemos el 1
        return 1L;
    }

    @GetMapping("/{idPesaje}/parcialidades")
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<List<ParcialidadResponseDTO>> verDetalle(@PathVariable Long idPesaje) {
        // Retorna la lista de camiones/parcialidades asociados al pesaje
        return ResponseEntity.ok(service.listarParcialidadesPorPesaje(idPesaje));
    }

    @GetMapping("/medidas")
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<List<MedidaPeso>> listarMedidas() {
        return ResponseEntity.ok(service.listarMedidas());
    }
}