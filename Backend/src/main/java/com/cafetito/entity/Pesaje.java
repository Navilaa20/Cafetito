package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pesajes", schema = "agricultor")
@Data
public class Pesaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_agricultor", nullable = false)
    private Long idAgricultor; //

    @Column(name = "id_cuenta")
    private Long idCuenta;

    @ManyToOne
    @JoinColumn(name = "id_medida", nullable = false)
    private MedidaPeso medidaPeso; // Relación con la tabla de medidas

    @Column(name = "peso_total_actual", nullable = false)
    private BigDecimal pesoTotalActual;

    @Column(name = "pesaje_acumulado")
    private BigDecimal pesajeAcumulado = BigDecimal.ZERO;

    @Column(name = "cant_parcialidades")
    private Integer cantidadParcialidades = 0;

    private String estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}