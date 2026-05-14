package com.cafetito.exception;

public class EstadoTransportistaIgualException extends RuntimeException {

    public EstadoTransportistaIgualException(String estado) {
        super("El transportista ya se encuentra " + (estado != null ? estado : "en ese estado"));
    }
}
