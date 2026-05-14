package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcialidades_bascula", schema = "peso_cabal")
@Data
public class ParcialidadBascula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_serial")
    private Long id;

    @Column(name = "id_parcialidad_ref")
    private Long idParcialidadRef;

    @Column(name = "peso_bascula")
    private Double pesoBascula;

    @Column(name = "peso_en_kg")
    private Double pesoEnKg;

    @Column(name = "id_usuario_pesaje")
    private Long idUsuarioPesaje;

    @Column(name = "fecha_peso")
    private LocalDateTime fechaPeso;
}