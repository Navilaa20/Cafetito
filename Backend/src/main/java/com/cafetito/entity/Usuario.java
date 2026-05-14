package com.cafetito.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "usuarios", schema = "public")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String password;

    @NotNull
    @Column(nullable = false)
    private Boolean activo = true;

    @NotNull
    @Convert(converter = RolConverter.class)
    @Column(nullable = false, length = 50)
    private Rol rol;

    @Column(name = "nit_agricultor", length = 50)
    private String nitAgricultor;

    public Usuario() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public String getNitAgricultor() { return nitAgricultor; }
    public void setNitAgricultor(String nitAgricultor) { this.nitAgricultor = nitAgricultor; }
}
