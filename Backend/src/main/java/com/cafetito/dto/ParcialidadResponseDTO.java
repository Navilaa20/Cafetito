package com.cafetito.dto;

import java.time.LocalDateTime;

public class ParcialidadResponseDTO {

    private Long idParcialidad;
    private String placaTransporte;
    private String nombreTransportista;
    private Double pesoEnviado;
    private String tipoDeMedida;
    private LocalDateTime fechaRecepcionParcialidad;
    private String detalle;
    private Boolean aceptado;

    public ParcialidadResponseDTO() {}

    public ParcialidadResponseDTO(Long idParcialidad, String placaTransporte, String nombreTransportista,
                                   Double pesoEnviado, String tipoDeMedida,
                                   LocalDateTime fechaRecepcionParcialidad, String detalle, Boolean aceptado) {
        this.idParcialidad = idParcialidad;
        this.placaTransporte = placaTransporte;
        this.nombreTransportista = nombreTransportista;
        this.pesoEnviado = pesoEnviado;
        this.tipoDeMedida = tipoDeMedida;
        this.fechaRecepcionParcialidad = fechaRecepcionParcialidad;
        this.detalle = detalle;
        this.aceptado = aceptado;
    }

    public ParcialidadResponseDTO(Long idParcialidad, Long idTransporte, String nombreTransportista, Double pesoEnviado, String tipoDeMedida, LocalDateTime fechaRecepcionParcialidad, String detalle, Boolean aceptado) {
    }

    public Long getIdParcialidad() { return idParcialidad; }
    public void setIdParcialidad(Long idParcialidad) { this.idParcialidad = idParcialidad; }

    public String getPlacaTransporte() { return placaTransporte; }
    public void setPlacaTransporte(String placaTransporte) { this.placaTransporte = placaTransporte; }

    public String getNombreTransportista() { return nombreTransportista; }
    public void setNombreTransportista(String nombreTransportista) { this.nombreTransportista = nombreTransportista; }

    public Double getPesoEnviado() { return pesoEnviado; }
    public void setPesoEnviado(Double pesoEnviado) { this.pesoEnviado = pesoEnviado; }

    public String getTipoDeMedida() { return tipoDeMedida; }
    public void setTipoDeMedida(String tipoDeMedida) { this.tipoDeMedida = tipoDeMedida; }

    public LocalDateTime getFechaRecepcionParcialidad() { return fechaRecepcionParcialidad; }
    public void setFechaRecepcionParcialidad(LocalDateTime fechaRecepcionParcialidad) { this.fechaRecepcionParcialidad = fechaRecepcionParcialidad; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public Boolean getAceptado() { return aceptado; }
    public void setAceptado(Boolean aceptado) { this.aceptado = aceptado; }
}
