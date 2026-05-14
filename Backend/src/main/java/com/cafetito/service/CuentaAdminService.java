package com.cafetito.service;

import com.cafetito.dto.*;
import com.cafetito.entity.*;
import com.cafetito.exception.CambioEstadoNoPermitidoException;
import com.cafetito.exception.CuentaNoEncontradaException;
import com.cafetito.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaAdminService {

    private static final double TOLERANCIA_PORCENTAJE = 5.0;
    private static final String RESULTADO_ACEPTADO = "Aceptado, en parametro";
    private static final String RESULTADO_FALTANTE = "Faltante";
    private static final String RESULTADO_SOBRANTE = "Sobrante";

    private final CuentaRepository cuentaRepository;
    private final ParcialidadRepository parcialidadRepository;
    private final TransportistaRepository transportistaRepository;
    private final PesajeRepository pesajeRepository;
    private final UsuarioRepository usuarioRepository;

    public CuentaAdminService(CuentaRepository cuentaRepository,
                              ParcialidadRepository parcialidadRepository,
                              TransportistaRepository transportistaRepository,
                              PesajeRepository pesajeRepository,
                              UsuarioRepository usuarioRepository) {
        this.cuentaRepository = cuentaRepository;
        this.parcialidadRepository = parcialidadRepository;
        this.transportistaRepository = transportistaRepository;
        this.pesajeRepository = pesajeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<CuentaResponseDTO> listarTodas() {
        return cuentaRepository.findAll().stream()
                .map(this::toCuentaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CuentaResponseDTO> buscarPorFecha(LocalDate fecha) {
        if (fecha == null) return listarTodas();
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return cuentaRepository.findByFechaCreacionBetween(inicio, fin).stream()
                .map(this::toCuentaResponseDTO)
                .collect(Collectors.toList());
    }

    public CuentaResponseDTO buscarPorNumero(Long idCuenta) {
        Cuenta c = cuentaRepository.findById(idCuenta)
                .orElseThrow(CuentaNoEncontradaException::new);
        return toCuentaResponseDTO(c);
    }

    public List<CuentaResponseDTO> filtrarPorEstado(EstadoCuenta estado) {
        if (estado == null) return listarTodas();
        return cuentaRepository.findByEstadoCuenta(estado).stream()
                .map(this::toCuentaResponseDTO)
                .collect(Collectors.toList());
    }

    public CuentaDetalleResponseDTO obtenerDetalle(Long idCuenta) {
        Cuenta c = cuentaRepository.findById(idCuenta)
                .orElseThrow(CuentaNoEncontradaException::new);
        CuentaDetalleResponseDTO dto = new CuentaDetalleResponseDTO();
        dto.setIdCuenta(c.getIdCuenta());
        dto.setNitAgricultor(c.getNitAgricultor());
        dto.setEstadoCuenta(c.getEstadoCuenta());
        dto.setCantidadParcialidades(c.getCantidadParcialidades());
        dto.setDiferenciaTotal(c.getDiferenciaTotal());
        dto.setTolerancia(c.getTolerancia());
        dto.setResultadoTolerancia(c.getResultadoTolerancia());

        List<Parcialidad> parcialidades = parcialidadRepository.buscarPorIdCuenta(idCuenta);
        List<ParcialidadResponseDTO> list = parcialidades.stream()
                .map(this::toParcialidadResponseDTO)
                .collect(Collectors.toList());
        dto.setParcialidades(list);
        return dto;
    }

    @Transactional
    public CuentaResponseDTO cambiarEstado(Long idCuenta, CambiarEstadoCuentaRequestDTO request) {
        Cuenta c = cuentaRepository.findById(idCuenta)
                .orElseThrow(CuentaNoEncontradaException::new);

        EstadoCuenta actual = c.getEstadoCuenta();
        EstadoCuenta nuevoEstado = request.getEstadoNuevo();

        // VALIDACIÓN CASO DE USO FA14: Solo se permite si está en PESAJE_FINALIZADO o CUENTA_CERRADA
        if (actual != EstadoCuenta.PESAJE_FINALIZADO && actual != EstadoCuenta.CUENTA_CERRADA) {
            throw new CambioEstadoNoPermitidoException("FA14: La cuenta se encuentra en estado: '" + actual + "'. No es posible cambiar de estado.");
        }

        // Actualizamos al estado que el Administrador eligió en el formulario de Angular (FA08)
        c.setEstadoCuenta(nuevoEstado);

        // CUMPLIMIENTO FA15: Si el nuevo estado es "CUENTA_CONFIRMADA", el sistema liquida y agrega textos
        if (nuevoEstado == EstadoCuenta.CUENTA_CONFIRMADA) {

            Double pesoEnviado = c.getPesoEnviado();
            Double pesoObtenido = c.getPesoTotalObtenido() != null ? c.getPesoTotalObtenido() : 0.0;
            Double porcentajeTolerancia = 0.05; // 5%

            Double diferencia = pesoObtenido - pesoEnviado;
            c.setDiferenciaTotal(diferencia);
            c.setTolerancia(5.0); // Guardamos el % para mostrar en UI

            Double rangoPermitido = pesoEnviado * porcentajeTolerancia;

            // Textos exigidos exactamente por FA15.2
            if (Math.abs(diferencia) <= rangoPermitido) {
                c.setResultadoTolerancia("Aceptado, en parametro");
            } else if (diferencia > rangoPermitido) {
                c.setResultadoTolerancia("Sobrante");
            } else {
                c.setResultadoTolerancia("Faltante");
            }
        }

        cuentaRepository.save(c);
        return toCuentaResponseDTO(c);
    }


    @Transactional
    public CuentaResponseDTO autorizarCuenta(Long idCuenta) {
        Cuenta c = cuentaRepository.findById(idCuenta)
                .orElseThrow(CuentaNoEncontradaException::new);

        if (c.getEstadoCuenta() != EstadoCuenta.CUENTA_CREADA) {
            throw new CambioEstadoNoPermitidoException("La cuenta ya ha sido procesada.");
        }

        c.setEstadoCuenta(EstadoCuenta.CUENTA_ABIERTA);
        return toCuentaResponseDTO(cuentaRepository.save(c));
    }

    public List<CuentaResponseDTO> buscarPorNit(String nit) {
        return cuentaRepository.findByNitAgricultor(nit).stream()
                .map(this::toCuentaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CuentaResponseDTO crearCuenta(CrearCuentaRequestDTO request) {
        Cuenta nueva = new Cuenta();
        nueva.setNitAgricultor(request.getNitAgricultor());
        nueva.setEstadoCuenta(EstadoCuenta.CUENTA_CREADA);
        return toCuentaResponseDTO(cuentaRepository.save(nueva));
    }

    public List<ParcialidadResponseDTO> obtenerParcialidadesPorCuenta(Long idCuenta) {
        return parcialidadRepository.buscarPorIdCuenta(idCuenta).stream()
                .map(this::toParcialidadResponseDTO)
                .collect(Collectors.toList());
    }

    // ====================================================================
    // ✅ MÉTODOS DE CREACIÓN DE CUENTAS DESDE PESAJE
    // ====================================================================

    @Transactional
    public CuentaResponseDTO crearCuentaDesdePesaje(Long idPesaje) {
        Pesaje pesaje = pesajeRepository.findById(idPesaje)
                .orElseThrow(() -> new RuntimeException("El pesaje solicitado no existe"));

        if (cuentaRepository.existsByIdPesaje(idPesaje)) {
            throw new RuntimeException("Este pesaje ya tiene una cuenta asociada.");
        }

        Usuario agricultor = usuarioRepository.findById(pesaje.getIdAgricultor())
                .orElseThrow(() -> new RuntimeException("Agricultor no encontrado"));

        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setIdPesaje(pesaje.getId());
        nuevaCuenta.setIdAgricultor(pesaje.getIdAgricultor());

        // Guardamos el username en el campo nitAgricultor para mostrar el nombre
        nuevaCuenta.setNitAgricultor(agricultor.getUsername());

        nuevaCuenta.setPesoEnviado(pesaje.getPesoTotalActual().doubleValue());
        nuevaCuenta.setEstadoCuenta(EstadoCuenta.CUENTA_ABIERTA);
        nuevaCuenta.setIdEstadoCuenta(2);

        String numeroGenerado = "CTA-A" + pesaje.getIdAgricultor() + "-P" + pesaje.getId();
        nuevaCuenta.setNoCuenta(numeroGenerado);

        // 1. Guardamos la cuenta oficial
        nuevaCuenta = cuentaRepository.saveAndFlush(nuevaCuenta);

        // 2. Actualizamos el pesaje original
        pesaje.setEstado("AUTORIZADO");
        pesaje.setIdCuenta(nuevaCuenta.getIdCuenta());
        pesajeRepository.save(pesaje);

        // 3. Rescate de parcialidades huérfanas
        List<Parcialidad> parcialidades = parcialidadRepository.findByIdPesaje(pesaje.getId());

        for (Parcialidad p : parcialidades) {
            p.setIdCuenta(nuevaCuenta); // Se pasa el objeto Cuenta completo
        }

        if (!parcialidades.isEmpty()) {
            parcialidadRepository.saveAll(parcialidades);

            nuevaCuenta.setCantidadParcialidades(parcialidades.size());
            cuentaRepository.save(nuevaCuenta);
        }

        return toCuentaResponseDTO(nuevaCuenta);
    }

    public List<PesajeResponseDTO> listarSolicitudesPendientes() {
        return pesajeRepository.findByEstado("PENDIENTE").stream()
                .map(p -> {
                    PesajeResponseDTO dto = new PesajeResponseDTO();
                    dto.setIdPesaje(p.getId());

                    // Buscamos el nombre para que se vea bien en el modal de Angular
                    String nombreAgricultor = usuarioRepository.findById(p.getIdAgricultor())
                            .map(Usuario::getUsername)
                            .orElse(String.valueOf(p.getIdAgricultor()));
                    dto.setNitAgricultor(nombreAgricultor);

                    dto.setPesoTotalActual(p.getPesoTotalActual());
                    dto.setFechaCreacion(p.getFechaCreacion());
                    dto.setEstadoPesaje(p.getEstado());
                    return dto;
                }).collect(Collectors.toList());
    }


    // --- MÉTODOS PRIVADOS DE APOYO ---

    private CuentaResponseDTO toCuentaResponseDTO(Cuenta c) {
        return new CuentaResponseDTO(
                c.getIdCuenta(),
                c.getNitAgricultor(),
                c.getPesoEnviado(),
                c.getCantidadParcialidades(),
                c.getFechaCreacion(),
                c.getEstadoCuenta()
        );
    }

    private ParcialidadResponseDTO toParcialidadResponseDTO(Parcialidad p) {
        String placa = String.valueOf(p.getIdTransporte());
        String nombreTransportista = resolveNombreTransportista(p.getIdTransportista());
        return new ParcialidadResponseDTO(
                p.getIdParcialidad(),
                placa,
                nombreTransportista,
                p.getPesoEnviado(),
                p.getTipoDeMedida(),
                p.getFechaRecepcionParcialidad(),
                p.getDetalle(),
                p.getAceptado()
        );
    }

    private String resolveNombreTransportista(Long idTransportista) {
        if (idTransportista == null) return "";
        return transportistaRepository.findById(idTransportista)
                .map(Transportista::getNombreCompleto)
                .orElse("");
    }
}