package com.cafetito.dto;

import com.cafetito.entity.EstadoCuenta;

public class CambiarEstadoCuentaRequestDTO {

    private Long idCuenta;
    private String estadoActual;
    private EstadoCuenta estadoNuevo;

    public Long getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Long idCuenta) { this.idCuenta = idCuenta; }

    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }

    public EstadoCuenta getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(EstadoCuenta estadoNuevo) { this.estadoNuevo = estadoNuevo; }
}
