package com.cafetito.controller;

import com.cafetito.entity.Color;
import com.cafetito.entity.Linea;
import com.cafetito.entity.Marca;
import com.cafetito.entity.TipoPlaca;
import com.cafetito.repository.ColorRepository;
import com.cafetito.repository.LineaRepository;
import com.cafetito.repository.MarcaRepository;
import com.cafetito.repository.TipoPlacaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    private final MarcaRepository marcaRepository;
    private final ColorRepository colorRepository;
    private final LineaRepository lineaRepository;
    private final TipoPlacaRepository tipoPlacaRepository; // ✅ 1. Declarar

    public CatalogoController(MarcaRepository marcaRepo,
                              ColorRepository colorRepo,
                              LineaRepository lineaRepo,
                              TipoPlacaRepository tipoPlacaRepo) {
        this.marcaRepository = marcaRepo;
        this.colorRepository = colorRepo;
        this.lineaRepository = lineaRepo;
        this.tipoPlacaRepository = tipoPlacaRepo;
    }

    @GetMapping("/marcas")
    public List<Marca> getMarcas() {
        return marcaRepository.findAll();
    }

    @GetMapping("/colores")
    public List<Color> getColores() {
        return colorRepository.findAll();
    }

    // Obtener todas las líneas
    @GetMapping("/lineas")
    public List<Linea> getLineas() {
        return lineaRepository.findAll();
    }

    // ✅ Obtener líneas filtradas por marca para Angular
    @GetMapping("/marcas/{marcaId}/lineas")
    public List<Linea> getLineasPorMarca(@PathVariable Integer marcaId) {
        return lineaRepository.findByIdMarca(marcaId);
    }

    // Obtener todas las líneas
    @GetMapping("/tipos-placa")
    public ResponseEntity<List<TipoPlaca>> getTiposPlaca() {
        return ResponseEntity.ok(tipoPlacaRepository.findAll());
    }

}
