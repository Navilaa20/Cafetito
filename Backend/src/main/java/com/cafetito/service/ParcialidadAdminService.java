package com.cafetito.service;

import com.cafetito.dto.ParcialidadResponseDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.cafetito.dto.ParcialidadDetalleResponseDTO;
import com.cafetito.entity.Parcialidad;
import com.cafetito.entity.Transporte;
import com.cafetito.entity.Transportista;
import com.cafetito.exception.ParcialidadNoEncontradaException;
import com.cafetito.exception.TransporteInactivoException;
import com.cafetito.exception.TransportistaInactivoException;
import com.cafetito.repository.ParcialidadRepository;
import com.cafetito.repository.TransporteRepository;
import com.cafetito.repository.TransportistaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class ParcialidadAdminService {

    private static final int QR_WIDTH = 200;
    private static final int QR_HEIGHT = 200;
    private static final String DETALLE_ESPERA = "Espera de ingreso";
    private static final String DETALLE_RECHAZADO = "Rechazado";


    @Value("${cafetito.frontend.url:https://cafetito-front.onrender.com}")
    private String frontendUrl;

    private final ParcialidadRepository parcialidadRepository;
    private final TransporteRepository transporteRepository;
    private final TransportistaRepository transportistaRepository;

    public ParcialidadAdminService(ParcialidadRepository parcialidadRepository,
                                   TransporteRepository transporteRepository,
                                   TransportistaRepository transportistaRepository) {
        this.parcialidadRepository = parcialidadRepository;
        this.transporteRepository = transporteRepository;
        this.transportistaRepository = transportistaRepository;
    }

    public ParcialidadDetalleResponseDTO obtenerDetalle(Long idParcialidad) {
        Parcialidad p = parcialidadRepository.findById(idParcialidad)
                .orElseThrow(ParcialidadNoEncontradaException::new);

        ParcialidadDetalleResponseDTO dto = new ParcialidadDetalleResponseDTO();
        dto.setIdCuenta(p.getIdCuenta().getIdCuenta());
        dto.setIdParcialidad(p.getIdParcialidad());
        dto.setAceptado(p.getAceptado());

        //Armamos la URL exacta hacia la ruta de tu frontend en Angular
        String qrContent = frontendUrl + "/dashboard/administrador/cuentas/"
                + p.getIdCuenta().getIdCuenta()
                + "/parcialidades/"
                + p.getIdParcialidad();

        dto.setQrCode(generarQRBase64(qrContent));

        Transporte t = transporteRepository.findById(p.getIdTransporte()).orElse(null);
        if (t != null) {
            ParcialidadDetalleResponseDTO.TransporteResumenDTO tr = new ParcialidadDetalleResponseDTO.TransporteResumenDTO();
            tr.setPlaca(t.getPlaca());
            tr.setEstado(t.getActivo());
            tr.setObservaciones(t.getObservaciones());
            dto.setTransporte(tr);
        }

        Transportista ts = transportistaRepository.findById(p.getIdTransportista()).orElse(null);
        if (ts != null) {
            ParcialidadDetalleResponseDTO.TransportistaResumenDTO tsr = new ParcialidadDetalleResponseDTO.TransportistaResumenDTO();
            tsr.setCui(ts.getCui());
            tsr.setNombre(ts.getNombreCompleto());
            tsr.setEstado(ts.getEstado());
            tsr.setObservaciones(ts.getObservaciones());
            dto.setTransportista(tsr);
        }

        return dto;
    }

    @Transactional
    public void recibirParcialidad(Long idParcialidad) {

        Parcialidad p = parcialidadRepository.findById(idParcialidad)
                .orElseThrow(ParcialidadNoEncontradaException::new);

        // Validación FA16: Piloto Activo
        Transportista ts = transportistaRepository.findById(p.getIdTransportista())
                .orElseThrow(ParcialidadNoEncontradaException::new);
        if (!Boolean.TRUE.equals(ts.getEstado())) {
            throw new TransportistaInactivoException(ts.getCui());
        }

        if (p.getAceptado() != null) {
            throw new IllegalStateException("La parcialidad ya fue procesada anteriormente.");
        }

        // Búsqueda del Vehículo
        Transporte t = transporteRepository.findById(p.getIdTransporte())
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado"));

        // Validación FA17: Transporte Activo
        if (!Boolean.TRUE.equals(t.getActivo())) {
            throw new TransporteInactivoException(t.getPlaca());
        }

        // Si pasa ambos candados, se autoriza el ingreso
        p.setFechaRecepcionParcialidad(LocalDateTime.now());
        p.setDetalle(DETALLE_ESPERA);
        p.setAceptado(true);
        parcialidadRepository.save(p);

        liberarFlota(p);
    }

    @Transactional
    public void rechazarParcialidad(Long idParcialidad) {
        Parcialidad p = parcialidadRepository.findById(idParcialidad)
                .orElseThrow(ParcialidadNoEncontradaException::new);

        if (p.getAceptado() != null) {
            throw new IllegalStateException("La parcialidad ya fue procesada anteriormente.");
        }

        p.setDetalle(DETALLE_RECHAZADO);
        p.setAceptado(false);
        parcialidadRepository.save(p);

        liberarFlota(p);
    }

    private String generarQRBase64(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);

            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generando QR", e);
        }
    }

    private void liberarFlota(Parcialidad parcialidad) {
        // 1. Liberar al Piloto
        if (parcialidad.getIdTransportista() != null) {
            transportistaRepository.findById(parcialidad.getIdTransportista()).ifPresent(piloto -> {
                piloto.setDisponible(true); // Vuelve a estar libre
                piloto.setPesajeAsociado(null);
                transportistaRepository.save(piloto);
            });
        }

        // 2. Liberar al Camión
        if (parcialidad.getIdTransporte() != null) {
            transporteRepository.findById(parcialidad.getIdTransporte()).ifPresent(camion -> {
                camion.setDisponible(true); // Vuelve a estar libre
                transporteRepository.save(camion);
            });
        }
    }
}