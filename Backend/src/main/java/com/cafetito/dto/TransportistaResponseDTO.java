package com.cafetito.dto;

import com.fasterxml.jackson.annotation.JsonProperty; // 👈 AGREGA ESTA IMPORTACIÓN
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportistaResponseDTO {
    private Long idTransportista;
    private String cui;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String tipoLicencia;
    private LocalDate fechaVencimientoLicencia;
    private Boolean estado;
    private Boolean disponible;

    @JsonProperty("nombreAgricultor") // 👈 ESTO OBLIGA A JACKSON A INCLUIRLO
    private String nombreAgricultor;

    private Long pesajeAsociado;
    private String observaciones;
}