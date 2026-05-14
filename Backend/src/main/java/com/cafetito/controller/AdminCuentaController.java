package com.cafetito.controller;

import com.cafetito.dto.*;
import com.cafetito.entity.EstadoCuenta;
import com.cafetito.service.CuentaAdminService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/cuentas")
public class AdminCuentaController {

    private final CuentaAdminService service;
    private final CuentaAdminService cuentaAdminService;

    public AdminCuentaController(CuentaAdminService service, CuentaAdminService cuentaAdminService) {
        this.service = service;
        this.cuentaAdminService = cuentaAdminService;
    }

    @GetMapping
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<List<CuentaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> buscar(
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "idCuenta", required = false) Long idCuenta,
            @RequestParam(value = "nit", required = false) String nit) { // <-- NUEVO PARÁMETRO

        if (idCuenta != null) {
            return ResponseEntity.ok(service.buscarPorNumero(idCuenta));
        }
        if (fecha != null) {
            return ResponseEntity.ok(service.buscarPorFecha(fecha));
        }
        if (nit != null && !nit.isEmpty()) {
            // Llama a un nuevo mtdo en el servicio que busca por NIT
            return ResponseEntity.ok(service.buscarPorNit(nit));
        }
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<List<CuentaResponseDTO>> filtrarPorEstado(
            @RequestParam(value = "estado", required = false) EstadoCuenta estado) {
        return ResponseEntity.ok(service.filtrarPorEstado(estado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<CuentaDetalleResponseDTO> obtenerDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerDetalle(id));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<CuentaResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoCuentaRequestDTO dto) {
        return ResponseEntity.ok(service.cambiarEstado(id, dto));
    }

    // Cumple con FA03: Crear cuenta
    @PostMapping
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<CuentaResponseDTO> crearCuenta(@Valid @RequestBody CrearCuentaRequestDTO dto) {
        CuentaResponseDTO nuevaCuenta = service.crearCuenta(dto);
        // Devuelve 201 CREATED como pide la RN03
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
    }

    // Cumple con FA09 (Beneficio) y FA12 (Peso Cabal)
    @GetMapping("/{idCuenta}/parcialidades")
    @PreAuthorize("hasAnyRole('BENEFICIO', 'PESOCABAL')")
    public ResponseEntity<List<ParcialidadResponseDTO>> obtenerParcialidadesDeCuenta(
            @PathVariable Long idCuenta) {

        List<ParcialidadResponseDTO> parcialidades = service.obtenerParcialidadesPorCuenta(idCuenta);
        return ResponseEntity.ok(parcialidades);
    }

    @PutMapping("/{id}/autorizar")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<CuentaResponseDTO> autorizarCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(service.autorizarCuenta(id));
    }

    @GetMapping("/solicitudes")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<List<PesajeResponseDTO>> listarSolicitudes() {
        return ResponseEntity.ok(service.listarSolicitudesPendientes());
    }

    @PostMapping("/solicitudes/{idPesaje}/generar-cuenta")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<CuentaResponseDTO> generarCuenta(@PathVariable Long idPesaje) {
        return ResponseEntity.ok(service.crearCuentaDesdePesaje(idPesaje));
    }

}
