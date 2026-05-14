package com.cafetito.exception;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException() {
        super("Usuario no encontrado.");
    }

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
}
