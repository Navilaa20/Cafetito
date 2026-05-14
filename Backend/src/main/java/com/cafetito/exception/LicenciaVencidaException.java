package com.cafetito.exception;

public class LicenciaVencidaException extends RuntimeException {

    public LicenciaVencidaException() {
        super("La licencia se encuentra vencida");
    }

    public LicenciaVencidaException(String message) {
        super(message);
    }
}
