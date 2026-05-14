package com.cafetito.exception;

public class EstadoTransporteIgualException extends RuntimeException {

    public EstadoTransporteIgualException(String estado) {
        super("El transporte ya se encuentra " + (estado != null ? estado : "en ese estado"));
    }
}
