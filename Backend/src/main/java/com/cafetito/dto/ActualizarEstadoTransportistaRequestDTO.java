package com.cafetito.dto;

import jakarta.validation.constraints.NotNull;

public class ActualizarEstadoTransportistaRequestDTO {

    private String cui;
    @NotNull(message = "El estado es obligatorio")
    private Boolean nuevoEstado;
    @NotNull(message = "Las observaciones son obligatorias")
    private String observaciones;

    public String getCui() { return cui; }
    public void setCui(String cui) { this.cui = cui; }

    public Boolean getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(Boolean nuevoEstado) { this.nuevoEstado = nuevoEstado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
