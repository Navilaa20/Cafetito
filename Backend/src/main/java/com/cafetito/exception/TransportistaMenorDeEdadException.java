package com.cafetito.exception;

public class TransportistaMenorDeEdadException extends RuntimeException {

    public TransportistaMenorDeEdadException() {
        super("El transportista es menor de edad");
    }

    public TransportistaMenorDeEdadException(String message) {
        super(message);
    }
}
