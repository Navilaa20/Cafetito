package com.cafetito.controller;

import com.cafetito.dto.ActualizarEstadoTransporteRequestDTO;
import com.cafetito.dto.TransporteRequestDTO;
import com.cafetito.dto.TransporteResponseDTO;
import com.cafetito.service.TransporteService;
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
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TransporteController {

    private final TransporteService service;

    public TransporteController(TransporteService service) {
        this.service = service;
    }

    // --- AGRICULTOR ---
    @PostMapping("/transportes")
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<TransporteResponseDTO> crear(@Valid @RequestBody TransporteRequestDTO dto) {
        Long idUsuario = getIdUsuarioFromAuth();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearTransporte(dto, idUsuario));
    }

    @GetMapping("/transportes")
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<List<TransporteResponseDTO>> listarParaAgricultor() {
        Long idUsuario = getIdUsuarioFromAuth();
        return ResponseEntity.ok(service.listarPorAgricultor(idUsuario));
    }

    @GetMapping("/transportes/disponibles")
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<List<TransporteResponseDTO>> listarDisponibles() {
        Long idUsuario = getIdUsuarioFromAuth();
        return ResponseEntity.ok(service.listarDisponiblesPorAgricultor(idUsuario));
    }

    // --- ADMINISTRACIÓN (BENEFICIO) ---
    @GetMapping("/admin/transportes/todos")
    @PreAuthorize("hasAnyAuthority('BENEFICIO', 'ROLE_BENEFICIO')")
    public ResponseEntity<List<TransporteResponseDTO>> listarTodosAdmin() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/admin/transportes/filtrar")
    @PreAuthorize("hasAnyAuthority('BENEFICIO', 'ROLE_BENEFICIO')")
    public ResponseEntity<List<TransporteResponseDTO>> filtrarAdmin(@RequestParam(name = "activo") Boolean activo) {
        return ResponseEntity.ok(service.filtrarPorEstado(activo));
    }

    @GetMapping("/admin/transportes/buscar")
    @PreAuthorize("hasAnyAuthority('BENEFICIO', 'ROLE_BENEFICIO')")
    public ResponseEntity<List<TransporteResponseDTO>> buscarPorPlaca(@RequestParam(name = "placa") String placa) {
        return ResponseEntity.ok(service.buscarPorPlaca(placa));
    }

    @PutMapping("/admin/transportes/{id}/estado")
    @PreAuthorize("hasAnyAuthority('BENEFICIO', 'ROLE_BENEFICIO')")
    public ResponseEntity<TransporteResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoTransporteRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarEstado(id, dto));
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