package com.cafetito.dto;

import java.time.LocalDateTime;

public class AgricultorDetalleResponseDTO extends AgricultorResponseDTO {

    private long cantidadCuentas;
    private long cantidadTransportes;
    private long cantidadTransportistas;

    public AgricultorDetalleResponseDTO() {}

    public AgricultorDetalleResponseDTO(String nitAgricultor, String nombre, Boolean activo,
                                        String observaciones, LocalDateTime fecha,
                                        long cantidadCuentas, long cantidadTransportes, long cantidadTransportistas) {
        super(nitAgricultor, nombre, activo, observaciones, fecha);
        this.cantidadCuentas = cantidadCuentas;
        this.cantidadTransportes = cantidadTransportes;
        this.cantidadTransportistas = cantidadTransportistas;
    }

    public long getCantidadCuentas() { return cantidadCuentas; }
    public void setCantidadCuentas(long cantidadCuentas) { this.cantidadCuentas = cantidadCuentas; }
    public long getCantidadTransportes() { return cantidadTransportes; }
    public void setCantidadTransportes(long cantidadTransportes) { this.cantidadTransportes = cantidadTransportes; }
    public long getCantidadTransportistas() { return cantidadTransportistas; }
    public void setCantidadTransportistas(long cantidadTransportistas) { this.cantidadTransportistas = cantidadTransportistas; }
}