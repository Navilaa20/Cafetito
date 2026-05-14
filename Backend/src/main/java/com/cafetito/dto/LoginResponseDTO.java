package com.cafetito.dto;

import com.cafetito.entity.Rol;

public class LoginResponseDTO {

    private String token;
    private Rol rol;
    private String username;
    private String nitAgricultor;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, Rol rol, String username, String nitAgricultor) {
        this.token = token;
        this.rol = rol;
        this.username = username;
        this.nitAgricultor = nitAgricultor;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNitAgricultor() { return nitAgricultor; }
    public void setNitAgricultor(String nitAgricultor) { this.nitAgricultor = nitAgricultor; }
}
