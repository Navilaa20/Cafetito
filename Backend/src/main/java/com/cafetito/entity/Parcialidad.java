package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "parcialidades", schema = "agricultor")
@Data
public class Parcialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parcialidad")
    private Long idParcialidad;

    @Column(name = "id_pesaje", nullable = false)
    private Long idPesaje;

    @ManyToOne
    @JoinColumn(name = "id_cuenta")
    private Cuenta idCuenta; // Relación con el esquema de beneficio

    @Column(name = "id_transporte", nullable = false, length = 20)
    private Long idTransporte;

    @Column(name = "id_transportista", nullable = false)
    private Long idTransportista;

    @Column(name = "fecha_recepcion_parcialidad")
    private LocalDateTime fechaRecepcionParcialidad;

    @Column(name = "peso_enviado")
    private Double pesoEnviado;

    @Column(name = "peso_bascula")
    private Double pesoBascula;

    @Column(name = "diferencia_peso")
    private Double diferenciaPeso;

    @Column(name = "aceptado")
    private Boolean aceptado;

    @Column(length = 500)
    private String observaciones;

    @Column(length = 100)
    private String detalle;

    @Column(nullable = false)
    private Boolean boleta = false;

    @Column(name = "fecha_boleta")
    private LocalDateTime fechaBoleta;

    @PrePersist
    protected void onCreate() {
        this.fechaRecepcionParcialidad = LocalDateTime.now();
    }

    @Column(name = "tipo_de_medida", length = 50)
    private String tipoDeMedida;

    @Column(name = "peso_declarado")
    private Double pesoDeclarado;

    @Column (name = "peso_en_kg")
    private Double pesoEnKg;

    public Parcialidad() {}

}
