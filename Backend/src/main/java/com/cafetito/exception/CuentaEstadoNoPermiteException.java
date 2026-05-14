package com.cafetito.exception;

public class CuentaEstadoNoPermiteException extends RuntimeException {

    public CuentaEstadoNoPermiteException(String estado) {
        super("La cuenta se encuentra en estado: '" + (estado != null ? estado : "") + "' no es posible agregar mas parcialidades");
    }
}
