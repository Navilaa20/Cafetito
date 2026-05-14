package com.cafetito.exception;

public class TransporteNoEncontradoException extends RuntimeException {

    public TransporteNoEncontradoException() {
        super("No existen registros");
    }

    public TransporteNoEncontradoException(String id) {
        super("No existen registros");
    }
}
