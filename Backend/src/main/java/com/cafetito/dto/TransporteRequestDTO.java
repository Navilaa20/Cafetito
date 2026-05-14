package com.cafetito.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransporteRequestDTO {

    @NotNull(message = "El tipo de placa es obligatorio.") // ✅ Ahora es Integer
    private Integer idTipoPlaca;

    @NotBlank(message = "El numero de placa es obligatorio.")
    private String numeroPlaca;

    @NotNull(message = "La marca es obligatoria.")
    private Integer marca;

    @NotNull(message = "El color es obligatorio.")
    private Integer color;

    @NotNull(message = "La linea es obligatoria.")
    private Integer linea;

    @NotNull(message = "El modelo (anio) es obligatorio.")
    private Integer modelo;

    private String observaciones;

    public TransporteRequestDTO() {}
}