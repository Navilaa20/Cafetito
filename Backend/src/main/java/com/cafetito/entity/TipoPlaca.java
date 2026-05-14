package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tipos_placa", schema = "agricultor")
public class TipoPlaca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 5)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String descripcion;
}