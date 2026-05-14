package com.cafetito.dto;

import jakarta.validation.constraints.NotBlank;

public class CrearCuentaRequestDTO {

    @NotBlank(message = "El NIT del agricultor es obligatorio")
    private String nitAgricultor;

    public CrearCuentaRequestDTO() {}

    public String getNitAgricultor() { return nitAgricultor; }
    public void setNitAgricultor(String nitAgricultor) { this.nitAgricultor = nitAgricultor; }
}