package com.cafetito.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CuentaPesajeDTO {
    private Long idCuenta;
    private LocalDateTime fechaEnvio;
    private String medidaPeso;
    private String estadoCuenta;
}