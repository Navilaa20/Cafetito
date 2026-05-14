package com.cafetito.dto;

import jakarta.validation.constraints.NotNull;

public class ActualizarEstadoTransporteRequestDTO {

    private String placa;
    @NotNull(message = "El estado es obligatorio")
    private Boolean nuevoEstado;
    @NotNull(message = "Las observaciones son obligatorias")
    private String observaciones;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public Boolean getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(Boolean nuevoEstado) { this.nuevoEstado = nuevoEstado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
