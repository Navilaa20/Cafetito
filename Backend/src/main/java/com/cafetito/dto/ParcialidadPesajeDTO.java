package com.cafetito.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ParcialidadPesajeDTO {
    private Long idParcialidad;
    private String placa;
    private String cuiTransportista;
    private String tipoMedida;
    private Double pesoBascula;
    private LocalDateTime fechaPeso;
    private String detalle;
}