package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transportes", schema = "agricultor")
public class Transporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "placa", unique = true, nullable = false)
    private String placa;

    @Column(name = "id_marca")
    private Integer idMarca;

    @Column(name = "id_color")
    private Integer idColor;

    @Column(name = "disponible") // ✅ En tu DB se llama 'disponible', no 'activo'
    private Boolean disponible = true;

    @Column(name = "pesaje_asociado") // ✅ Columna real de tu DB
    private Integer pesajeAsociado;

    @Column(name = "id_usuario") // ✅ Esta reemplaza a 'creado_por'
    private Long idUsuario;

    @Column(name = "id_tipo_placa") // ✅ Columna real de tu DB
    private Integer idTipoPlaca;

    @Column(name = "linea")
    private String linea;

    @Column(name = "modelo")
    private Integer modelo;

    @Column(name = "activo") // ✅ Tienes una columna llamada 'activo' al final
    private Boolean activo = true;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;


    public Transporte() {}

}