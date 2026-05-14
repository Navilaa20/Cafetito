package com.cafetito.service;

import com.cafetito.dto.ParcialidadResponseDTO;
import com.cafetito.dto.PesajeRequestDTO;
import com.cafetito.dto.PesajeResponseDTO;
import com.cafetito.entity.MedidaPeso;
import com.cafetito.entity.Pesaje;
import com.cafetito.repository.MedidaPesoRepository;
import com.cafetito.repository.ParcialidadRepository;
import com.cafetito.repository.PesajeRepository;
import com.cafetito.repository.TransportistaRepository;
import com.cafetito.repository.TransporteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PesajeAgricultorService {

    private final PesajeRepository pesajeRepository;
    private final MedidaPesoRepository medidaPesoRepository;
    private final ParcialidadRepository parcialidadRepository;
    private final TransportistaRepository transportistaRepository;
    private final TransporteRepository transporteRepository;

    public PesajeAgricultorService(PesajeRepository pesajeRepository,
                                   MedidaPesoRepository medidaPesoRepository,
                                   ParcialidadRepository parcialidadRepository,
                                   TransportistaRepository transportistaRepository,
                                   TransporteRepository transporteRepository) {
        this.pesajeRepository = pesajeRepository;
        this.medidaPesoRepository = medidaPesoRepository;
        this.parcialidadRepository = parcialidadRepository;
        this.transportistaRepository = transportistaRepository;
        this.transporteRepository = transporteRepository;
    }

    // ✅ PARA CREAR (Cumple con Paso 11 del Flujo Básico)
    public PesajeResponseDTO crear(PesajeRequestDTO dto, Long idUsuario) {
        MedidaPeso medida = medidaPesoRepository.findById(dto.getIdMedida())
                .orElseThrow(() -> new RuntimeException("Medida de peso no encontrada"));

        Pesaje p = new Pesaje();
        p.setIdAgricultor(idUsuario);
        p.setMedidaPeso(medida);
        p.setPesoTotalActual(dto.getPesoTotalActual());
        p.setEstado("PENDIENTE");
        p.setIdCuenta(null); // Inicia sin cuenta según el CU

        Pesaje guardado = pesajeRepository.save(p);
        return toDTO(guardado);
    }

    // ✅ PARA OBTENER POR ID (Para "Ver Detalle" FA01)
    public PesajeResponseDTO obtenerPorId(Long idPesaje, Long idUsuario) {
        Pesaje p = pesajeRepository.findById(idPesaje)
                .orElseThrow(() -> new RuntimeException("Pesaje no encontrado"));

        // Validación de seguridad
        if (!p.getIdAgricultor().equals(idUsuario)) {
            throw new RuntimeException("No tiene permiso para ver este pesaje");
        }
        return toDTO(p);
    }

    // FA03: Listar pesajes para la tabla principal
    public List<PesajeResponseDTO> listarPorAgricultor(Long idUsuario) {
        return pesajeRepository.findByIdAgricultor(idUsuario).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Dentro de PesajeAgricultorService
    public List<MedidaPeso> listarMedidas() {
        return medidaPesoRepository.findAll();
    }

    public List<ParcialidadResponseDTO> listarParcialidadesPorPesaje(Long idPesaje) {
        return parcialidadRepository.findByIdPesaje(idPesaje).stream()
                .map(p -> {
                    String nombreTr = transportistaRepository.findById(p.getIdTransportista())
                            .map(t -> t.getNombreCompleto())
                            .orElse("Desconocido");

                    String placa = transporteRepository.findById(p.getIdTransporte())
                            .map(t -> t.getPlaca())
                            .orElse("N/A");

                    return new ParcialidadResponseDTO(
                            p.getIdParcialidad(),
                            placa,
                            nombreTr,
                            p.getPesoEnviado(),
                            p.getTipoDeMedida(),
                            p.getFechaRecepcionParcialidad(),
                            p.getDetalle(),
                            p.getAceptado()
                    );
                }).collect(Collectors.toList());
    }

    private PesajeResponseDTO toDTO(Pesaje p) {
        PesajeResponseDTO dto = new PesajeResponseDTO();
        dto.setIdPesaje(p.getId());

        // ✅ Reconstruimos el formato exacto de la cuenta
        if (p.getIdCuenta() != null) {
            String numeroFormateado = "CTA-A" + p.getIdAgricultor() + "-P" + p.getId();
            dto.setNumeroCuenta(numeroFormateado);
        } else {
            dto.setNumeroCuenta("Pendiente");
        }

        dto.setPesoTotalActual(p.getPesoTotalActual());
        dto.setFechaCreacion(p.getFechaCreacion());
        dto.setEstadoPesaje(p.getEstado());
        dto.setMedidaDePeso(p.getMedidaPeso() != null ? p.getMedidaPeso().getNombre() : "N/A");

        long conteo = parcialidadRepository.countByIdPesaje(p.getId());
        dto.setCantidadParcialidades((int) conteo);

        return dto;
    }
}