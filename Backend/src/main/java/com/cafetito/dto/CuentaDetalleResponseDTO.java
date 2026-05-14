package com.cafetito.dto;

import com.cafetito.entity.EstadoCuenta;

import java.util.List;

public class CuentaDetalleResponseDTO {

    private Long idCuenta;
    private String nitAgricultor;
    private EstadoCuenta estadoCuenta;
    private Integer cantidadParcialidades;
    private Double diferenciaTotal;
    private Double tolerancia;
    private String resultadoTolerancia;
    private List<ParcialidadResponseDTO> parcialidades;

    public CuentaDetalleResponseDTO() {}

    public Long getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Long idCuenta) { this.idCuenta = idCuenta; }

    public String getNitAgricultor() { return nitAgricultor; }
    public void setNitAgricultor(String nitAgricultor) { this.nitAgricultor = nitAgricultor; }

    public EstadoCuenta getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(EstadoCuenta estadoCuenta) { this.estadoCuenta = estadoCuenta; }

    public Integer getCantidadParcialidades() { return cantidadParcialidades; }
    public void setCantidadParcialidades(Integer cantidadParcialidades) { this.cantidadParcialidades = cantidadParcialidades; }

    public Double getDiferenciaTotal() { return diferenciaTotal; }
    public void setDiferenciaTotal(Double diferenciaTotal) { this.diferenciaTotal = diferenciaTotal; }

    public Double getTolerancia() { return tolerancia; }
    public void setTolerancia(Double tolerancia) { this.tolerancia = tolerancia; }

    public String getResultadoTolerancia() { return resultadoTolerancia; }
    public void setResultadoTolerancia(String resultadoTolerancia) { this.resultadoTolerancia = resultadoTolerancia; }

    public List<ParcialidadResponseDTO> getParcialidades() { return parcialidades; }
    public void setParcialidades(List<ParcialidadResponseDTO> parcialidades) { this.parcialidades = parcialidades; }
}
