package com.cafetito.service;

import com.cafetito.dto.*;
import com.cafetito.entity.*;
import com.cafetito.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PesajeCabalService {

    private final CuentaRepository cuentaRepository;
    private final ParcialidadRepository parcialidadRepository;
    private final TransporteRepository transporteRepository;
    private final TransportistaRepository transportistaRepository;
    private final ParcialidadBasculaRepository parcialidadBasculaRepository;
    private final BoletaRepository boletaRepository;

    public PesajeCabalService(CuentaRepository cuentaRepository,
                              ParcialidadRepository parcialidadRepository,
                              TransporteRepository transporteRepository,
                              TransportistaRepository transportistaRepository,
                              ParcialidadBasculaRepository parcialidadBasculaRepository,
                              BoletaRepository boletaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.parcialidadRepository = parcialidadRepository;
        this.transporteRepository = transporteRepository;
        this.transportistaRepository = transportistaRepository;
        this.parcialidadBasculaRepository = parcialidadBasculaRepository;
        this.boletaRepository = boletaRepository;
    }

    public List<CuentaPesajeDTO> listarCuentasEnProceso() {
        return cuentaRepository.findAll().stream()
                .filter(c -> c.getEstadoCuenta() == EstadoCuenta.CUENTA_ABIERTA ||
                        c.getEstadoCuenta() == EstadoCuenta.PESAJE_INICIADO ||
                        c.getEstadoCuenta() == EstadoCuenta.PESAJE_FINALIZADO)
                .map(c -> {
                    CuentaPesajeDTO dto = new CuentaPesajeDTO();
                    dto.setIdCuenta(c.getIdCuenta());
                    dto.setFechaEnvio(c.getFechaCreacion());
                    dto.setEstadoCuenta(c.getEstadoCuenta().name());
                    // Asumimos "kg" por defecto si no tienes un campo global de medida en la cuenta
                    dto.setMedidaPeso("kg");
                    return dto;
                }).collect(Collectors.toList());
    }

    public List<ParcialidadPesajeDTO> listarParcialidadesPorCuenta(Long idCuenta) {
        List<Parcialidad> parcialidades = parcialidadRepository.buscarPorIdCuenta(idCuenta);

        return parcialidades.stream().map(p -> {
            ParcialidadPesajeDTO dto = new ParcialidadPesajeDTO();
            dto.setIdParcialidad(p.getIdParcialidad());
            dto.setTipoMedida(p.getTipoDeMedida());
            dto.setPesoBascula(p.getPesoBascula());
            // Si la base de datos tiene una fecha de peso, la enviamos, sino null
            dto.setFechaPeso(p.getPesoBascula() != null ? LocalDateTime.now() : null);
            dto.setDetalle(p.getDetalle());

            transporteRepository.findById(p.getIdTransporte())
                    .ifPresent(t -> dto.setPlaca(t.getPlaca()));

            transportistaRepository.findById(p.getIdTransportista())
                    .ifPresent(ts -> dto.setCuiTransportista(ts.getCui()));

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void actualizarPesoParcialidad(Long idParcialidad, ActualizarPesoRequestDTO request) {
        // 1. ESQUEMA AGRICULTOR: Actualizar Parcialidad Original
        Parcialidad p = parcialidadRepository.findById(idParcialidad)
                .orElseThrow(() -> new RuntimeException("Parcialidad no encontrada"));

        p.setPesoBascula(request.getPesoObtenido());
        p.setTipoDeMedida(request.getMedidaPeso());
        p.setDetalle("Pesaje Realizado");
        parcialidadRepository.save(p);

        // 2. ESQUEMA PESO_CABAL: Insertar auditoría de báscula
        ParcialidadBascula pb = new ParcialidadBascula();
        pb.setIdParcialidadRef(p.getIdParcialidad());
        pb.setPesoBascula(request.getPesoObtenido());
        pb.setPesoEnKg(request.getPesoObtenido()); // Ajustar si hay conversión
        pb.setFechaPeso(LocalDateTime.now());
        pb.setIdUsuarioPesaje(1L); // Aquí iría el ID del usuario logueado
        parcialidadBasculaRepository.save(pb);

        // 3. ESQUEMA PESO_CABAL: Insertar registro de Boleta (FA03)
        Boleta b = new Boleta();
        b.setIdParcialidadBascula(pb.getId()); // FK a la tabla anterior
        b.setPesoObtenido(request.getPesoObtenido());
        b.setFechaBoleta(LocalDateTime.now());
        b.setIdUsuarioBoleta(1L);

        // Guardamos un "Snapshot" (foto del momento) de placa y CUI
        // Esto es vital si el transporte cambia en el futuro, la boleta queda intacta
        transporteRepository.findById(p.getIdTransporte()).ifPresent(t -> b.setPlacaSnapshot(t.getPlaca()));
        transportistaRepository.findById(p.getIdTransportista()).ifPresent(ts -> b.setCuiSnapshot(ts.getCui()));

        boletaRepository.save(b);

        // 4. ESQUEMA BENEFICIO: Actualizar la Cuenta Global
        Cuenta c = p.getIdCuenta();

        // Actualizamos el acumulado en la cuenta
        Double nuevoTotal = (c.getPesoTotalObtenido() != null ? c.getPesoTotalObtenido() : 0.0) + request.getPesoObtenido();
        c.setPesoTotalObtenido(nuevoTotal);

        // --- NUEVA LÓGICA: DETECCIÓN DE FIN DE PESAJE ---

        // Contamos cuántas parcialidades en total tiene esta cuenta
        long totalParcialidades = parcialidadRepository.buscarPorIdCuenta(c.getIdCuenta()).size();

        // Contamos cuántas de esas parcialidades ya tienen un peso registrado en báscula
        long pesajesCompletados = parcialidadRepository.buscarPorIdCuenta(c.getIdCuenta()).stream()
                .filter(p_aux -> p_aux.getPesoBascula() != null)
                .count();

        // Validamos: Si ya se pesaron todas, finalizamos. Si no, solo iniciamos.
        if (pesajesCompletados == totalParcialidades) {
            c.setEstadoCuenta(EstadoCuenta.PESAJE_FINALIZADO);
        } else if (c.getEstadoCuenta() == EstadoCuenta.CUENTA_ABIERTA) {
            c.setEstadoCuenta(EstadoCuenta.PESAJE_INICIADO);
        }

        cuentaRepository.save(c);
    }
}