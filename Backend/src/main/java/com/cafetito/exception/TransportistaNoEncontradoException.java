package com.cafetito.exception;

public class TransportistaNoEncontradoException extends RuntimeException {

    public TransportistaNoEncontradoException() {
        super("No existen registros");
    }

    public TransportistaNoEncontradoException(Long id) {
        super("No existen registros");
    }
}
