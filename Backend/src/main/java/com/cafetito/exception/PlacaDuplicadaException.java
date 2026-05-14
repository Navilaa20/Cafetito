package com.cafetito.exception;

public class PlacaDuplicadaException extends RuntimeException {

    public PlacaDuplicadaException(String placa) {
        super("Ya existe un transporte registrado con la placa " + placa);
    }
}
