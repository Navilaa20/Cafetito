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

        String qrContent = "CUENTA:" + p.getIdCuenta().getIdCuenta() + ":PARCIALIDAD:" + p.getIdParcialidad();
        dto.setQrCode(generarQRBase64(qrContent));

        // ✅ Corregido: p.getIdTransporte() ya es Long, se usa directo
        // Dentro de ParcialidadAdminService.java, busca la línea ~58 y déjala así:

        Transporte t = transporteRepository.findById(p.getIdTransporte()).orElse(null);
        if (t != null) {
            ParcialidadDetalleResponseDTO.TransporteResumenDTO tr = new ParcialidadDetalleResponseDTO.TransporteResumenDTO();

            // ✅ CORREGIDO: t.getPlaca() ya tiene la placa completa.
            // Eliminamos el t.getTipoPlaca() que daba error de compilación.
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

        // ✅ Validación FA17: Transporte Activo
        if (!Boolean.TRUE.equals(t.getActivo())) {
            throw new TransporteInactivoException(t.getPlaca());
        }

        // Si pasa ambos candados, se autoriza el ingreso
        p.setFechaRecepcionParcialidad(LocalDateTime.now());
        p.setDetalle(DETALLE_ESPERA);
        p.setAceptado(true);
        parcialidadRepository.save(p);
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
    }

    private String generarQRBase64(String content) {
        try {
            // Borramos el objeto nulo y la variable sin uso.
            // Solo necesitamos convertir el 'content' que recibimos a QR.
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);

            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generando QR", e);
        }
    }
}