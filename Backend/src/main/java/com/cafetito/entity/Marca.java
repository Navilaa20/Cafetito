package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "marcas", schema = "agricultor")
@Data
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "creado_por")
    private String creadoPor = "navila";


    public Marca() {}
}