package com.cafetito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PesajeResponseDTO {
    private Long idPesaje;
    private String numeroCuenta; // Para mostrar "Pendiente" o el ID
    private BigDecimal pesoTotalActual;
    private Integer cantidadParcialidades;
    private LocalDateTime fechaCreacion;
    private String estadoPesaje;
    private String medidaDePeso;
    private String nitAgricultor;

}

