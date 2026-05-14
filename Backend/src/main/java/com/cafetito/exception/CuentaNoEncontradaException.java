package com.cafetito.exception;

public class CuentaNoEncontradaException extends RuntimeException {

    public CuentaNoEncontradaException() {
        super("No existen registros");
    }
}
