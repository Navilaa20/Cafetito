package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "colores", schema = "agricultor")
@Data
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30)
    private String nombre;

    @Column(name = "creado_por")
    private String creadoPor = "navila";

    public Color() {}
}