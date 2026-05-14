package com.cafetito.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transportistas", schema = "agricultor")
public class Transportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") //
    private Long idTransportista;

    @Column(unique = true, nullable = false, length = 20)
    private String cui;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "tipo_licencia", nullable = false, length = 50)
    private String tipoLicencia;

    @Column(name = "fecha_vencimiento_licencia", nullable = false)
    private LocalDate fechaVencimientoLicencia;

    // ✅ MAGIA AQUÍ: En Java se llama 'estado', pero en Postgres se guarda en 'disponible'
    @Column(name = "disponible", nullable = false)
    private Boolean estado = true;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "pesaje_asociado")
    private Long pesajeAsociado;

    @Column(length = 500)
    private String observaciones;

    @CreationTimestamp // ✅ Genera el timestamp automáticamente al crear
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "transportista_transporte",
            schema = "agricultor", // Especificamos el esquema
            joinColumns = @JoinColumn(name = "id_transportista"),
            inverseJoinColumns = @JoinColumn(name = "id_transporte")
    )

    private List<Transporte> transportes = new java.util.ArrayList<>();

    public Transportista() {}

    // --- GETTERS Y SETTERS ---
    public Long getIdTransportista() { return idTransportista; }
    public void setIdTransportista(Long idTransportista) { this.idTransportista = idTransportista; }
    public String getCui() { return cui; }
    public void setCui(String cui) { this.cui = cui; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getTipoLicencia() { return tipoLicencia; }
    public void setTipoLicencia(String tipoLicencia) { this.tipoLicencia = tipoLicencia; }
    public LocalDate getFechaVencimientoLicencia() { return fechaVencimientoLicencia; }
    public void setFechaVencimientoLicencia(LocalDate fechaVencimientoLicencia) { this.fechaVencimientoLicencia = fechaVencimientoLicencia; }

    // El getter y setter se siguen llamando 'Estado' para que tu Repository no se rompa
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Long getPesajeAsociado() { return pesajeAsociado; }
    public void setPesajeAsociado(Long pesajeAsociado) { this.pesajeAsociado = pesajeAsociado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<Transporte> getTransportes() { return transportes; }
    public void setTransportes(List<Transporte> transportes) { this.transportes = transportes; }
}