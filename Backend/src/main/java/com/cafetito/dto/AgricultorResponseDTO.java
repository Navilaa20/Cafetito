package com.cafetito.dto;

import java.time.LocalDateTime;

public class AgricultorResponseDTO {

    private String nitAgricultor;
    private String nombre;
    private Boolean activo;
    private String observaciones;
    private LocalDateTime fecha;

    public AgricultorResponseDTO() {}

    public AgricultorResponseDTO(String nitAgricultor, String nombre, Boolean activo, String observaciones, LocalDateTime fecha) {
        this.nitAgricultor = nitAgricultor;
        this.nombre = nombre;
        this.activo = activo;
        this.observaciones = observaciones;
        this.fecha = fecha;
    }

    public String getNitAgricultor() {
        return nitAgricultor;
    }

    public void setNitAgricultor(String nitAgricultor) {
        this.nitAgricultor = nitAgricultor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
