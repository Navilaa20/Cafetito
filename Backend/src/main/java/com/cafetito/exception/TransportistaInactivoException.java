package com.cafetito.exception;

public class TransportistaInactivoException extends RuntimeException {

    public TransportistaInactivoException(String cui) {
        super("El transportista " + (cui != null ? cui : "") + " no se encuentra activo.");
    }
}
