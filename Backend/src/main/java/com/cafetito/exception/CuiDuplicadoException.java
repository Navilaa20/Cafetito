package com.cafetito.exception;

public class CuiDuplicadoException extends RuntimeException {

    public CuiDuplicadoException(String cui) {
        super("Ya existe un transportista registrado con el CUI " + (cui != null ? cui : ""));
    }
}
