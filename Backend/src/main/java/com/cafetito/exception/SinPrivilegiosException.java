package com.cafetito.exception;

public class SinPrivilegiosException extends RuntimeException {

    public SinPrivilegiosException() {
        super("El usuario no cuenta con privilegios.");
    }

    public SinPrivilegiosException(String message) {
        super(message);
    }
}
