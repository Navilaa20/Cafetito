package com.cafetito.exception;

public class UsuarioInactivoException extends RuntimeException {

    public UsuarioInactivoException() {
        super("El usuario esta inactivo.");
    }

    public UsuarioInactivoException(String message) {
        super(message);
    }
}
