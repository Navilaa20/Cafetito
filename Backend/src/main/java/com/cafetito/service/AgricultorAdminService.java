package com.cafetito.service;

import com.cafetito.dto.AgricultorDetalleResponseDTO;
import com.cafetito.dto.AgricultorResponseDTO;
import com.cafetito.entity.Rol;
import com.cafetito.entity.Usuario;
import com.cafetito.repository.CuentaRepository;
import com.cafetito.repository.TransporteRepository;
import com.cafetito.repository.TransportistaRepository;
import com.cafetito.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgricultorAdminService {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final TransporteRepository transporteRepository;
    private final TransportistaRepository transportistaRepository;

    public AgricultorAdminService(UsuarioRepository usuarioRepository, CuentaRepository cuentaRepository,
                                  TransporteRepository transporteRepository, TransportistaRepository transportistaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
        this.transporteRepository = transporteRepository;
        this.transportistaRepository = transportistaRepository;
    }

    public List<AgricultorResponseDTO> listarAgricultores(String nit) {
        List<Usuario> usuarios;

        // Si envían un NIT (FA01), filtramos.
        if (nit != null && !nit.isEmpty()) {
            usuarios = usuarioRepository.findByRolAndNitAgricultorContainingIgnoreCase(Rol.ROLE_AGRICULTOR, nit);
        } else {
            usuarios = usuarioRepository.findByRol(Rol.ROLE_AGRICULTOR);
        }

        // Mapeamos lo que tenemos en la entidad hacia lo que pide el DTO (RN04 No. 9)
        return usuarios.stream().map(usuario -> new AgricultorResponseDTO(
                usuario.getNitAgricultor(),
                usuario.getUsername(), // Usamos el username porque no hay campo "nombre"
                usuario.getActivo(),
                "Sin observaciones", //
                LocalDateTime.now() // Fecha generada al momento
        )).collect(Collectors.toList());
    }

    // CUMPLE Paso 9: Ver Detalle y Contadores
    public AgricultorDetalleResponseDTO obtenerDetalle(String nit) {
        // ✅ CAMBIO: Usar findByNitAgricultor en lugar de findByUsername
        Usuario u = usuarioRepository.findByNitAgricultor(nit)
                .orElseThrow(() -> new RuntimeException("Agricultor no encontrado"));

        long cuentas = cuentaRepository.countByIdAgricultor(u.getId());
        long transportes = transporteRepository.countByIdAgricultor(u.getId());
        long transportistas = transportistaRepository.countByIdAgricultor(u.getId());

        return new AgricultorDetalleResponseDTO(
                u.getNitAgricultor(),
                u.getUsername(),
                u.getActivo(),
                "Sin observaciones",
                LocalDateTime.now(),
                cuentas,
                transportes,
                transportistas
        );
    }
}