package com.cafetito.exception;

public class ModeloInvalidoException extends RuntimeException {

    public ModeloInvalidoException() {
        super("El anio del vehiculo debe estar entre 1980 y el anio actual");
    }

    public ModeloInvalidoException(String message) {
        super(message);
    }
}
