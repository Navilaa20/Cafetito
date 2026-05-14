package com.cafetito.dto;

import com.cafetito.entity.EstadoCuenta;

import java.time.LocalDateTime;

public class CuentaResponseDTO {

    private Long idCuenta;
    private String nitAgricultor;
    private Double pesoEnviado;
    private Integer cantidadParcialidades;
    private LocalDateTime fechaCreacion;
    private EstadoCuenta estadoCuenta;

    public CuentaResponseDTO() {}

    public CuentaResponseDTO(Long idCuenta, String nitAgricultor, Double pesoEnviado,
                              Integer cantidadParcialidades, LocalDateTime fechaCreacion,
                              EstadoCuenta estadoCuenta) {
        this.idCuenta = idCuenta;
        this.nitAgricultor = nitAgricultor;
        this.pesoEnviado = pesoEnviado;
        this.cantidadParcialidades = cantidadParcialidades;
        this.fechaCreacion = fechaCreacion;
        this.estadoCuenta = estadoCuenta;
    }

    public Long getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Long idCuenta) { this.idCuenta = idCuenta; }

    public String getNitAgricultor() { return nitAgricultor; }
    public void setNitAgricultor(String nitAgricultor) { this.nitAgricultor = nitAgricultor; }

    public Double getPesoEnviado() { return pesoEnviado; }
    public void setPesoEnviado(Double pesoEnviado) { this.pesoEnviado = pesoEnviado; }

    public Integer getCantidadParcialidades() { return cantidadParcialidades; }
    public void setCantidadParcialidades(Integer cantidadParcialidades) { this.cantidadParcialidades = cantidadParcialidades; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public EstadoCuenta getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(EstadoCuenta estadoCuenta) { this.estadoCuenta = estadoCuenta; }
}
