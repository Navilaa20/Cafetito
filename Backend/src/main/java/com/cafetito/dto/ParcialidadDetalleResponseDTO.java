package com.cafetito.dto;

public class ParcialidadDetalleResponseDTO {

    private Long idCuenta;
    private Long idParcialidad;
    private String qrCode;
    private Boolean aceptado;
    private TransporteResumenDTO transporte;
    private TransportistaResumenDTO transportista;

    public ParcialidadDetalleResponseDTO() {}

    public Long getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Long idCuenta) { this.idCuenta = idCuenta; }

    public Long getIdParcialidad() { return idParcialidad; }
    public void setIdParcialidad(Long idParcialidad) { this.idParcialidad = idParcialidad; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public Boolean getAceptado() { return aceptado; }
    public void setAceptado(Boolean aceptado) { this.aceptado = aceptado; }

    public TransporteResumenDTO getTransporte() { return transporte; }
    public void setTransporte(TransporteResumenDTO transporte) { this.transporte = transporte; }

    public TransportistaResumenDTO getTransportista() { return transportista; }
    public void setTransportista(TransportistaResumenDTO transportista) { this.transportista = transportista; }

    public static class TransporteResumenDTO {
        private String placa;
        private Boolean estado;
        private String observaciones;

        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }
        public Boolean getEstado() { return estado; }
        public void setEstado(Boolean estado) { this.estado = estado; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    }

    public static class TransportistaResumenDTO {
        private String cui;
        private String nombre;
        private Boolean estado;
        private String observaciones;

        public String getCui() { return cui; }
        public void setCui(String cui) { this.cui = cui; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public Boolean getEstado() { return estado; }
        public void setEstado(Boolean estado) { this.estado = estado; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    }
}
