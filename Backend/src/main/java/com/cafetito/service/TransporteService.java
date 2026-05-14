package com.cafetito.service;

import com.cafetito.dto.ActualizarEstadoTransporteRequestDTO;
import com.cafetito.dto.TransporteRequestDTO;
import com.cafetito.dto.TransporteResponseDTO;
import com.cafetito.entity.TipoPlaca;
import com.cafetito.entity.Transporte;
import com.cafetito.entity.Usuario;
import com.cafetito.exception.*;
import com.cafetito.repository.TipoPlacaRepository;
import com.cafetito.repository.TransporteRepository;
import com.cafetito.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransporteService {

    private static final String PLACA_REGEX = "^\\d{3}[A-Za-z]{3}$";
    private static final String FORMATO_PLACA_ERROR = "Formato de placa incorrecto, ej: 000AAA";

    private final TransporteRepository repository;
    private final TipoPlacaRepository tipoPlacaRepository;
    private final UsuarioRepository usuarioRepository;// ✅ Inyectado

    public TransporteService(TransporteRepository repository,
                             TipoPlacaRepository tipoPlacaRepository,
                             UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.tipoPlacaRepository = tipoPlacaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public TransporteResponseDTO crearTransporte(TransporteRequestDTO dto, Long idUsuario) { // ✅ Recibe Long idUsuario
        String numeroPlaca = dto.getNumeroPlaca() != null ? dto.getNumeroPlaca().trim().toUpperCase() : "";

        if (!numeroPlaca.matches(PLACA_REGEX)) {
            throw new FormatoPlacaInvalidoException(FORMATO_PLACA_ERROR);
        }

        int anioActual = Year.now().getValue();
        if (dto.getModelo() == null || dto.getModelo() < 1980 || dto.getModelo() > anioActual) {
            throw new ModeloInvalidoException();
        }

        // ✅ Obtener el código de la placa (Ej: "C", "M")
        TipoPlaca tp = tipoPlacaRepository.findById(dto.getIdTipoPlaca())
                .orElseThrow(() -> new RuntimeException("Tipo de placa no encontrado"));

        String placaFinal = tp.getCodigo() + numeroPlaca;

        if (repository.existsByPlaca(placaFinal)) {
            throw new PlacaDuplicadaException(placaFinal);
        }

        Transporte tr = new Transporte();
        tr.setPlaca(placaFinal);
        tr.setIdTipoPlaca(dto.getIdTipoPlaca());
        tr.setIdMarca(dto.getMarca());
        tr.setIdColor(dto.getColor());
        tr.setLinea(String.valueOf(dto.getLinea())); // Convertido a String porque tu DB lo tiene varchar
        tr.setModelo(dto.getModelo());
        tr.setActivo(true);
        tr.setDisponible(true); // ✅ Añadido
        tr.setObservaciones(dto.getObservaciones() != null ? dto.getObservaciones().trim() : null);
        tr.setIdUsuario(idUsuario); // ✅ Asignado el idUsuario

        Transporte saved = repository.save(tr);
        return toDTO(saved);
    }

    public List<TransporteResponseDTO> listarPorAgricultor(Long idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TransporteResponseDTO> listarDisponiblesPorAgricultor(Long idUsuario) {
        return repository.findByIdUsuarioAndActivoTrue(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TransporteResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TransporteResponseDTO> filtrarPorEstado(Boolean activo) {
        // Si el estado es null, devolvemos todos (comportamiento de "Todos" en el select)
        if (activo == null) {
            return listarTodos();
        }
        // Buscamos en el repositorio por el campo 'activo'
        return repository.findByActivo(activo).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TransporteResponseDTO> buscarPorPlaca(String placa) {
        if (placa == null || placa.isBlank()) return listarTodos();
        return repository.findByPlacaContainingIgnoreCase(placa.trim()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public TransporteResponseDTO actualizarEstado(Long id, ActualizarEstadoTransporteRequestDTO dto) {
        Transporte t = repository.findById(id).orElseThrow(TransporteNoEncontradoException::new);

        if (t.getActivo().equals(dto.getNuevoEstado())) {
            String estadoTexto = t.getActivo() ? "Activo" : "Inactivo";
            throw new EstadoTransporteIgualException(estadoTexto);
        }

        t.setActivo(dto.getNuevoEstado());
        if (dto.getObservaciones() != null) t.setObservaciones(dto.getObservaciones().trim());

        return toDTO(repository.save(t));
    }

    private TransporteResponseDTO toDTO(Transporte t) {
        TransporteResponseDTO dto = new TransporteResponseDTO();
        dto.setId(t.getId());
        dto.setIdTipoPlaca(t.getIdTipoPlaca());
        dto.setPlaca(t.getPlaca());
        dto.setIdMarca(t.getIdMarca());
        dto.setIdColor(t.getIdColor());
        dto.setLinea(t.getLinea());
        dto.setModelo(t.getModelo());
        dto.setActivo(t.getActivo());
        dto.setObservaciones(t.getObservaciones());
        dto.setIdUsuario(t.getIdUsuario());
        dto.setFechaCreacion(t.getFechaCreacion());

        // ✅ BUSCAMOS EL NOMBRE DEL AGRICULTOR DUEÑO DEL VEHÍCULO
        if (t.getIdUsuario() != null) {
            String nombre = usuarioRepository.findById(t.getIdUsuario())
                    .map(Usuario::getUsername)
                    .orElse("Desconocido");
            dto.setNombreAgricultor(nombre); // 👈 Asegúrate que TransporteResponseDTO tenga este campo
        }

        return dto;
    }
}