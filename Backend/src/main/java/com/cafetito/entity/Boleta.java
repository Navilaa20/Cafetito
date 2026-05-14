package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "boletas", schema = "peso_cabal")
@Data
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_serial")
    private Long id;

    @Column(name = "id_parcialidad_bascula") // Corregido según tu imagen
    private Long idParcialidadBascula;

    @Column(name = "placa_snapshot")
    private String placaSnapshot;

    @Column(name = "cui_snapshot")
    private String cuiSnapshot;

    @Column(name = "peso_obtenido")
    private Double pesoObtenido;

    @Column(name = "id_usuario_boleta")
    private Long idUsuarioBoleta;

    @Column(name = "fecha_boleta")
    private LocalDateTime fechaBoleta;
}