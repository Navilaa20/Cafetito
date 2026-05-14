package com.cafetito.exception;

public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Credenciales invalidas.");
    }

    public CredencialesInvalidasException(String message) {
        super(message);
    }
}
