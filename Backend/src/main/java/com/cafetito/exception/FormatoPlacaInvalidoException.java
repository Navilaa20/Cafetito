package com.cafetito.exception;

public class FormatoPlacaInvalidoException extends RuntimeException {

    public FormatoPlacaInvalidoException() {
        super("Formato de placa incorrecto, ej: 000AAA");
    }

    public FormatoPlacaInvalidoException(String message) {
        super(message);
    }
}
