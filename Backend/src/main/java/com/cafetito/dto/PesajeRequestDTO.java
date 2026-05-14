package com.cafetito.dto;

import java.math.BigDecimal;

public class PesajeRequestDTO {

    private Long idMedida; // El ID de la medida (lb, kg, etc.) seleccionada en el combo
    private BigDecimal pesoTotalActual;

    public PesajeRequestDTO() {}

    public PesajeRequestDTO(Long idMedida, BigDecimal pesoTotalActual) {
        this.idMedida = idMedida;
        this.pesoTotalActual = pesoTotalActual;
    }

    public Long getIdMedida() { return idMedida; }
    public void setIdMedida(Long idMedida) { this.idMedida = idMedida; }

    public BigDecimal getPesoTotalActual() { return pesoTotalActual; }
    public void setPesoTotalActual(BigDecimal pesoTotalActual) { this.pesoTotalActual = pesoTotalActual; }
}