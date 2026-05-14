package com.cafetito.exception;

public class TransporteInactivoException extends RuntimeException {

    public TransporteInactivoException(String placa) {
        super("El transporte " + (placa != null ? placa : "") + " no se encuentra activo.");
    }
}
