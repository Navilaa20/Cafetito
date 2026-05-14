package com.cafetito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporteResponseDTO {
    private Long id;
    private Integer idTipoPlaca;
    private String placa;
    private Integer idMarca;
    private Integer idColor;
    private String linea;
    private Integer modelo;
    private Boolean activo;
    private String observaciones;
    private Long idUsuario;
    private LocalDateTime fechaCreacion;

    // ✅ AGREGA ESTE CAMPO:
    private String nombreAgricultor;
}