package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lineas", schema = "agricultor")
@Data
public class Linea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "id_marca", nullable = false)
    private Integer idMarca;

    @Column(name = "creado_por")
    private String creadoPor = "navila";

    public Linea() {}
}