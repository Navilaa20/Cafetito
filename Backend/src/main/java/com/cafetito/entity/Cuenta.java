package com.cafetito.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas", schema = "beneficio")
public class Cuenta {

    // 1. Apuntamos a "id" que es la verdadera PK en tu base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idCuenta; // Mantenemos este nombre para no romper tus DTOs

    // 2. ✅ COLUMNAS FALTANTES AÑADIDAS
    @Column(name = "no_cuenta", length = 50)
    private String noCuenta;

    @Column(name = "id_agricultor")
    private Long idAgricultor;

    @Column(name = "id_estado_cuenta")
    private Integer idEstadoCuenta;

    // 3. El resto de tus columnas...
    @Column(name = "nit_agricultor", length = 50)
    private String nitAgricultor;

    @Column(name = "id_pesaje")
    private Long idPesaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuenta", length = 30)
    private EstadoCuenta estadoCuenta = EstadoCuenta.CUENTA_CREADA;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "peso_enviado")
    private Double pesoEnviado = 0.0;

    @Column(name = "peso_total_obtenido")
    private Double pesoTotalObtenido = 0.0;

    @Column(name = "diferencia_total")
    private Double diferenciaTotal = 0.0;

    @Column(name = "cantidad_parcialidades")
    private Integer cantidadParcialidades = 0;

    @Column(name = "tolerancia")
    private Double tolerancia = 5.0;

    @Column(name = "resultado_tolerancia", length = 100)
    private String resultadoTolerancia;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    public Cuenta() {}

    // ====== GETTERS Y SETTERS ======
    public Long getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Long idCuenta) { this.idCuenta = idCuenta; }

    public String getNoCuenta() { return noCuenta; }
    public void setNoCuenta(String noCuenta) { this.noCuenta = noCuenta; }

    public Long getIdAgricultor() { return idAgricultor; }
    public void setIdAgricultor(Long idAgricultor) { this.idAgricultor = idAgricultor; }

    public Integer getIdEstadoCuenta() { return idEstadoCuenta; }
    public void setIdEstadoCuenta(Integer idEstadoCuenta) { this.idEstadoCuenta = idEstadoCuenta; }

    public String getNitAgricultor() { return nitAgricultor; }
    public void setNitAgricultor(String nitAgricultor) { this.nitAgricultor = nitAgricultor; }

    public Long getIdPesaje() { return idPesaje; }
    public void setIdPesaje(Long idPesaje) { this.idPesaje = idPesaje; }

    public EstadoCuenta getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(EstadoCuenta estadoCuenta) { this.estadoCuenta = estadoCuenta; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Double getPesoEnviado() { return pesoEnviado; }
    public void setPesoEnviado(Double pesoEnviado) { this.pesoEnviado = pesoEnviado; }

    public Double getPesoTotalObtenido() { return pesoTotalObtenido; }
    public void setPesoTotalObtenido(Double pesoTotalObtenido) { this.pesoTotalObtenido = pesoTotalObtenido; }

    public Double getDiferenciaTotal() { return diferenciaTotal; }
    public void setDiferenciaTotal(Double diferenciaTotal) { this.diferenciaTotal = diferenciaTotal; }

    public Integer getCantidadParcialidades() { return cantidadParcialidades; }
    public void setCantidadParcialidades(Integer cantidadParcialidades) { this.cantidadParcialidades = cantidadParcialidades; }

    public Double getTolerancia() { return tolerancia; }
    public void setTolerancia(Double tolerancia) { this.tolerancia = tolerancia; }

    public String getResultadoTolerancia() { return resultadoTolerancia; }
    public void setResultadoTolerancia(String resultadoTolerancia) { this.resultadoTolerancia = resultadoTolerancia; }
}