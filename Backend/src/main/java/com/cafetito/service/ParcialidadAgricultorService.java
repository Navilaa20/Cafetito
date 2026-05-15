package com.cafetito.service;

import com.cafetito.dto.ParcialidadRequestDTO;
import com.cafetito.dto.ParcialidadResponseDTO;
import com.cafetito.entity.Parcialidad;
import com.cafetito.entity.Pesaje;
import com.cafetito.entity.Transportista;
import com.cafetito.entity.Transporte; // ✅ Asegúrate de tener este import
import com.cafetito.repository.ParcialidadRepository;
import com.cafetito.repository.PesajeRepository;
import com.cafetito.repository.TransportistaRepository;
import com.cafetito.repository.TransporteRepository; // ✅ Asegúrate de tener este import
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParcialidadAgricultorService {

    private static final double PESO_MINIMO = 1.0;

    private final ParcialidadRepository parcialidadRepository;
    private final PesajeRepository pesajeRepository;
    private final TransportistaRepository transportistaRepository;
    private final TransporteRepository transporteRepository; // ✅ 1. Inyectamos el repo de Transportes

    public ParcialidadAgricultorService(ParcialidadRepository parcialidadRepository,
                                        PesajeRepository pesajeRepository,
                                        TransportistaRepository transportistaRepository,
                                        TransporteRepository transporteRepository) { // ✅ Agregado al constructor
        this.parcialidadRepository = parcialidadRepository;
        this.pesajeRepository = pesajeRepository;
        this.transportistaRepository = transportistaRepository;
        this.transporteRepository = transporteRepository;
    }

    @Transactional
    public ParcialidadResponseDTO crearParcialidad(ParcialidadRequestDTO dto, Long idUsuario) {

        // 1. Validar el Pesaje
        Pesaje pesaje = pesajeRepository.findById(dto.getIdPesaje())
                .orElseThrow(() -> new RuntimeException("El pesaje no existe"));

        if (dto.getPeso() == null) {
            throw new IllegalArgumentException("El peso es obligatorio");
        }

        double pesoConvertido = dto.getPeso().doubleValue();
        if (pesoConvertido < PESO_MINIMO) {
            throw new IllegalArgumentException("El valor minimo es 1");
        }

        // 👇 ================= CANDADO Y BLOQUEO DE FLOTA ================= 👇
        // 2. Buscar y validar al Transportista (Piloto)
        Transportista piloto = transportistaRepository.findById(dto.getIdTransportista())
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));

        // EL CANDADO: Evitar que un piloto tome dos viajes al mismo tiempo
        if (!piloto.getDisponible()) {
            throw new RuntimeException("El transportista " + piloto.getNombreCompleto() + " ya se encuentra en otro viaje.");
        }

        // Lo bloqueamos para este viaje
        piloto.setDisponible(false);
        piloto.setPesajeAsociado(pesaje.getId());
        transportistaRepository.save(piloto);

        // 3. Buscar y bloquear el Transporte (Camión)
        Transporte camion = transporteRepository.findById(Long.parseLong(dto.getIdTransporte()))
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado"));

        // Como el camión solo maneja un estado, lo ponemos inactivo mientras viaja.
        camion.setDisponible(false);
        transporteRepository.save(camion);

        // 4. Crear la boleta de Parcialidad
        Parcialidad p = new Parcialidad();
        p.setIdPesaje(pesaje.getId());
        p.setIdCuenta(null);
        p.setIdTransporte(Long.parseLong(dto.getIdTransporte()));
        p.setIdTransportista(dto.getIdTransportista());

        // Asegurarnos de poblar todas las columnas de peso
        p.setPesoEnviado(pesoConvertido);
        p.setPesoDeclarado(pesoConvertido);
        p.setPesoEnKg(pesoConvertido);

        p.setTipoDeMedida(dto.getTipoDeMedida() != null ? dto.getTipoDeMedida().trim() : null);
        p.setAceptado(null);

        Parcialidad saved = parcialidadRepository.save(p);

        // 5. Actualizar el contador del padre (Pesaje)
        int cantidadActual = pesaje.getCantidadParcialidades() != null ? pesaje.getCantidadParcialidades() : 0;
        pesaje.setCantidadParcialidades(cantidadActual + 1);
        pesajeRepository.save(pesaje);

        return toDTO(saved);
    }

    public List<ParcialidadResponseDTO> listarPorPesaje(Long idPesaje) {
        return parcialidadRepository.findByIdPesaje(idPesaje).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ParcialidadResponseDTO toDTO(Parcialidad p) {
        // Obtenemos el nombre del transportista
        String nombreTransportista = transportistaRepository.findById(p.getIdTransportista())
                .map(Transportista::getNombreCompleto)
                .orElse("");

        // ✅ 2. Hacemos exactamente lo mismo para el Transporte (Extraemos la placa)
        String placaTransporte = transporteRepository.findById(p.getIdTransporte())
                .map(Transporte::getPlaca) // ⚠️ OJO: Si en tu entidad Transporte el método se llama de otra forma (ej. getNumeroPlaca()), cámbialo aquí.
                .orElse("");

        return new ParcialidadResponseDTO(
                p.getIdParcialidad(),
                placaTransporte, // ✅ 3. Pasamos la placa real en lugar de String.valueOf(...)
                nombreTransportista,
                p.getPesoEnviado(),
                p.getTipoDeMedida(),
                p.getFechaRecepcionParcialidad(),
                p.getDetalle(),
                p.getAceptado()
        );
    }
}