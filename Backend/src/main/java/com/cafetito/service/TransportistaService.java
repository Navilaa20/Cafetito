package com.cafetito.service;

import com.cafetito.dto.ActualizarEstadoTransportistaRequestDTO;
import com.cafetito.dto.TransportistaRequestDTO;
import com.cafetito.dto.TransportistaResponseDTO;
import com.cafetito.entity.Transportista;
import com.cafetito.exception.CuiDuplicadoException;
import com.cafetito.exception.EstadoTransportistaIgualException;
import com.cafetito.exception.LicenciaVencidaException;
import com.cafetito.exception.TransportistaMenorDeEdadException;
import com.cafetito.exception.TransportistaNoEncontradoException;
import com.cafetito.repository.CuentaRepository;
import com.cafetito.repository.TransportistaRepository;
import com.cafetito.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportistaService {

    private static final String CUI_SOLO_13_DIGITOS = "CUI debe contener 13 digitos";
    private static final String NOMBRE_APELLIDO_MINIMO = "Debe colocar un nombre y un apellido como minimo";

    private final TransportistaRepository repository;
    private final UsuarioRepository usuarioRepository;

    public TransportistaService(TransportistaRepository repository,
                                UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository; //
    }

    public TransportistaResponseDTO crearTransportista(TransportistaRequestDTO dto, Long idUsuario) {
        String cuiNormalizado = normalizarCui(dto.getCui());
        if (cuiNormalizado == null || cuiNormalizado.length() != 13 || !cuiNormalizado.matches("\\d{13}")) {
            throw new IllegalArgumentException(CUI_SOLO_13_DIGITOS);
        }
        if (!cumpleNombreApellido(dto.getNombreCompleto())) {
            throw new IllegalArgumentException(NOMBRE_APELLIDO_MINIMO);
        }

        if (repository.existsByCui(cuiNormalizado)) {
            throw new CuiDuplicadoException(cuiNormalizado);
        }

        int edad = Period.between(dto.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < 18) {
            throw new TransportistaMenorDeEdadException();
        }

        if (dto.getFechaVencimientoLicencia().isBefore(LocalDate.now())) {
            throw new LicenciaVencidaException();
        }

        Transportista t = new Transportista();
        t.setCui(cuiNormalizado);
        t.setNombreCompleto(dto.getNombreCompleto().trim());
        t.setFechaNacimiento(dto.getFechaNacimiento());
        t.setTipoLicencia(dto.getTipoLicencia());
        t.setFechaVencimientoLicencia(dto.getFechaVencimientoLicencia());
        t.setEstado(true);
        t.setIdUsuario(idUsuario);
        t.setFechaRegistro(LocalDateTime.now());

        Transportista saved = repository.save(t);
        return toDTO(saved);
    }

    // ✅ 1. Cambiamos String nitAgricultor por Long idUsuario
    // ✅ Y llamamos a repository.findByIdUsuario
    public List<TransportistaResponseDTO> listarPorAgricultor(Long idUsuario) {
        return repository.findByIdUsuario(idUsuario)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ 2. Cambiamos String nitAgricultor por Long idUsuario
    // ✅ Y llamamos a repository.findByIdUsuarioAndEstadoTrue
    public List<TransportistaResponseDTO> listarDisponiblesPorAgricultor(Long idUsuario) {
        return repository.findByIdUsuarioAndEstadoTrue(idUsuario).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TransportistaResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TransportistaResponseDTO> buscarPorCui(String cui) {
        if (cui == null || cui.isBlank()) {
            return listarTodos();
        }
        return repository.findByCuiContainingIgnoreCase(cui.trim())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TransportistaResponseDTO> filtrarPorEstado(Boolean activo) {
        if (activo == null) {
            return listarTodos();
        }
        // ✅ Asegúrate que llame a transportistaRepository (el que tiene el findByEstado)
        return repository.findByEstado(activo)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TransportistaResponseDTO actualizarEstado(Long id, ActualizarEstadoTransportistaRequestDTO dto) {
        Transportista t = repository.findById(id)
                .orElseThrow(TransportistaNoEncontradoException::new);
        Boolean nuevoEstado = dto.getNuevoEstado();
        if (Boolean.valueOf(t.getEstado()).equals(nuevoEstado)) {
            String estadoTexto = Boolean.TRUE.equals(t.getEstado()) ? "Activo" : "Inactivo";
            throw new EstadoTransportistaIgualException(estadoTexto);
        }
        t.setEstado(nuevoEstado);
        t.setObservaciones(dto.getObservaciones() != null ? dto.getObservaciones().trim() : null);
        Transportista saved = repository.save(t);
        return toDTO(saved);
    }

    private static String normalizarCui(String cui) {
        if (cui == null) return null;
        String n = cui.replaceAll("[\\s-]", "");
        return n.matches("\\d{13}") ? n : null;
    }

    private static boolean cumpleNombreApellido(String nombre) {
        if (nombre == null || nombre.isBlank()) return false;
        String[] palabras = nombre.trim().split("\\s+");
        if (palabras.length < 2) return false;
        for (String p : palabras) {
            if (p.length() < 3) return false;
        }
        return true;
    }

    private TransportistaResponseDTO toDTO(Transportista t) {
        String nombreAgricultor = "Desconocido";

        if (t.getIdUsuario() != null) {
            nombreAgricultor = usuarioRepository.findById(t.getIdUsuario())
                    .map(u -> u.getUsername())
                    .orElse("Desconocido");
        }

        return new TransportistaResponseDTO(
                t.getIdTransportista(),
                t.getCui(),
                t.getNombreCompleto(),
                t.getFechaNacimiento(),
                t.getTipoLicencia(),
                t.getFechaVencimientoLicencia(),
                t.getEstado(),
                t.getEstado(),
                nombreAgricultor, // ✅ Se asigna aquí
                t.getPesajeAsociado(),
                t.getObservaciones()
        );
    }


}
