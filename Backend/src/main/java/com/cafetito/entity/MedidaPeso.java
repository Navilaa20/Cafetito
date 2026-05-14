package com.cafetito.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "medidas_peso", schema = "agricultor") // ✅ Schema corregido a singular
@Data
public class MedidaPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String simbolo;

    // Usamos BigDecimal para coincidir con el NUMERIC(18,10) de tu función SQL
    @Column(name = "factor_kg")
    private BigDecimal factorKg;

    @Column(name = "creado_por")
    private String creadoPor;

    private String abreviatura;

    public MedidaPeso() {}
}