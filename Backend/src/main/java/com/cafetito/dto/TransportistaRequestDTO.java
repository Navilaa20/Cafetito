package com.cafetito.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransportistaRequestDTO {

    @NotBlank(message = "El CUI es obligatorio.")
    private String cui;

    @NotBlank(message = "El nombre completo es obligatorio.")
    private String nombreCompleto;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El tipo de licencia es obligatorio.")
    private String tipoLicencia;

    @NotNull(message = "La fecha de vencimiento de licencia es obligatoria.")
    private LocalDate fechaVencimientoLicencia;

    private LocalDateTime fechaRegistro;

    public TransportistaRequestDTO() {}

    public String getCui() { return cui; }
    public void setCui(String cui) { this.cui = cui; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTipoLicencia() { return tipoLicencia; }
    public void setTipoLicencia(String tipoLicencia) { this.tipoLicencia = tipoLicencia; }

    public LocalDate getFechaVencimientoLicencia() { return fechaVencimientoLicencia; }
    public void setFechaVencimientoLicencia(LocalDate fechaVencimientoLicencia) { this.fechaVencimientoLicencia = fechaVencimientoLicencia; }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
