package com.cafetito.exception;

public class ParcialidadNoEncontradaException extends RuntimeException {

    public ParcialidadNoEncontradaException() {
        super("No existen registros");
    }
}
