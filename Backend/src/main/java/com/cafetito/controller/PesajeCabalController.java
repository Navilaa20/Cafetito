package com.cafetito.controller;

import com.cafetito.dto.*;
import com.cafetito.service.PesajeCabalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pesaje-cabal")
@CrossOrigin(origins = "*")
public class PesajeCabalController {

    private final PesajeCabalService pesajeCabalService;

    public PesajeCabalController(PesajeCabalService pesajeCabalService) {
        this.pesajeCabalService = pesajeCabalService;
    }

    @GetMapping("/cuentas")
    public ResponseEntity<List<CuentaPesajeDTO>> listarCuentas() {
        return ResponseEntity.ok(pesajeCabalService.listarCuentasEnProceso());
    }

    @GetMapping("/cuentas/{id}/parcialidades")
    public ResponseEntity<List<ParcialidadPesajeDTO>> listarParcialidades(@PathVariable Long id) {
        return ResponseEntity.ok(pesajeCabalService.listarParcialidadesPorCuenta(id));
    }

    @PutMapping("/parcialidades/{id}/peso")
    public ResponseEntity<Void> actualizarPeso(@PathVariable Long id, @RequestBody ActualizarPesoRequestDTO request) {
        pesajeCabalService.actualizarPesoParcialidad(id, request);
        return ResponseEntity.ok().build();
    }
}