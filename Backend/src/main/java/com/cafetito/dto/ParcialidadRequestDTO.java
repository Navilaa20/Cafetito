package com.cafetito.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ParcialidadRequestDTO {

    // ✅ ¡Cambiado! Ahora pedimos el Pesaje (1 o 2), ya no la Cuenta
    @NotNull(message = "El pesaje es obligatorio")
    private Long idPesaje;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor a 0")
    private BigDecimal peso;

    @NotNull(message = "El transporte es obligatorio")
    private String idTransporte;

    @NotNull(message = "El transportista es obligatorio")
    private Long idTransportista;

    @NotNull(message = "El tipo de medida es obligatorio")
    private String tipoDeMedida;

    // --- GETTERS Y SETTERS ---
    public Long getIdPesaje() { return idPesaje; }
    public void setIdPesaje(Long idPesaje) { this.idPesaje = idPesaje; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public String getIdTransporte() { return idTransporte; }
    public void setIdTransporte(String idTransporte) { this.idTransporte = idTransporte; }

    public Long getIdTransportista() { return idTransportista; }
    public void setIdTransportista(Long idTransportista) { this.idTransportista = idTransportista; }

    public String getTipoDeMedida() { return tipoDeMedida; }
    public void setTipoDeMedida(String tipoDeMedida) { this.tipoDeMedida = tipoDeMedida; }
}