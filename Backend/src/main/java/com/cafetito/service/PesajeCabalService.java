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
        // 1. ESQUEMA AGRICULTOR: Actualizar Parcialidad con el peso de báscula
        Parcialidad p = parcialidadRepository.findById(idParcialidad)
                .orElseThrow(() -> new RuntimeException("Parcialidad no encontrada"));

        p.setPesoBascula(request.getPesoObtenido());
        p.setTipoDeMedida(request.getMedidaPeso());
        p.setDetalle("Pesaje Realizado");
        parcialidadRepository.save(p);

        // 2. ESQUEMA PESO_CABAL: Auditoría de báscula
        ParcialidadBascula pb = new ParcialidadBascula();
        pb.setIdParcialidadRef(p.getIdParcialidad());
        pb.setPesoBascula(request.getPesoObtenido());
        pb.setPesoEnKg(request.getPesoObtenido());
        pb.setFechaPeso(LocalDateTime.now());
        pb.setIdUsuarioPesaje(1L);
        parcialidadBasculaRepository.save(pb);

        // 3. ESQUEMA PESO_CABAL: Generar Boleta con Snapshot de datos
        Boleta b = new Boleta();
        b.setIdParcialidadBascula(pb.getId());
        b.setPesoObtenido(request.getPesoObtenido());
        b.setFechaBoleta(LocalDateTime.now());
        b.setIdUsuarioBoleta(1L);

        transporteRepository.findById(p.getIdTransporte()).ifPresent(t -> b.setPlacaSnapshot(t.getPlaca()));
        transportistaRepository.findById(p.getIdTransportista()).ifPresent(ts -> b.setCuiSnapshot(ts.getCui()));

        boletaRepository.save(b);

        // 4. ESQUEMA BENEFICIO: Lógica de Estado de la Cuenta
        Cuenta c = p.getIdCuenta();
        if (c == null) return; // Seguridad en caso de parcialidad huérfana

        // Actualizar el acumulado de la cuenta
        Double pesoActual = c.getPesoTotalObtenido() != null ? c.getPesoTotalObtenido() : 0.0;
        Double nuevoAcumulado = pesoActual + request.getPesoObtenido();
        c.setPesoTotalObtenido(nuevoAcumulado);

        //Obtenemos el peso prometido
        Double pesoDeclarado = c.getPesoEnviado() != null ? c.getPesoEnviado(): 0.0;

        Double limiteInferiorPermitido = pesoDeclarado * 0.95; // 5% de tolerancia

        // --- RECUENTO DINÁMICO DE PARCIALIDADES ---
        List<Parcialidad> todas = parcialidadRepository.buscarPorIdCuenta(c.getIdCuenta());
        long totalCamiones = todas.size();
        // Contamos las que ya tienen peso, incluyendo explícitamente la actual para evitar errores de caché
        long camionesConPeso = todas.stream()
                .filter(par -> par.getPesoBascula() != null || par.getIdParcialidad().equals(idParcialidad))
                .count();

        boolean todosLosCamionesPesados = camionesConPeso >= totalCamiones;

        if (nuevoAcumulado >= pesoDeclarado) {
            // CASO 1: Ya llegó al 100% o hay SOBRANTE. Se finaliza de inmediato.
            c.setEstadoCuenta(EstadoCuenta.PESAJE_FINALIZADO);

        } else if (nuevoAcumulado >= limiteInferiorPermitido && todosLosCamionesPesados) {
            // CASO 2: Faltante ACEPTABLE (Quedó entre el 95% y 99%).
            // Solo se finaliza si ya no hay más camiones en camino.
            c.setEstadoCuenta(EstadoCuenta.PESAJE_FINALIZADO);

        } else {
            // CASO 3: Faltante GRAVE (< 95%) o aún vienen camiones en camino.
            // La cuenta se queda viva. El beneficio pedirá que manden otro camión.
            c.setEstadoCuenta(EstadoCuenta.PESAJE_INICIADO);
        }
        cuentaRepository.save(c);
    }
}