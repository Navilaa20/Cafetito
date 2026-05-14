package com.cafetito.dto;
import lombok.Data;

@Data
public class ActualizarPesoRequestDTO {
    private Double pesoObtenido;
    private String medidaPeso;
    private String observaciones;
}