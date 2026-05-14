package com.cafetito.service;

import com.cafetito.dto.LoginRequestDTO;
import com.cafetito.dto.LoginResponseDTO;
import com.cafetito.entity.Rol;
import com.cafetito.entity.Usuario;
import com.cafetito.exception.CredencialesInvalidasException;
import com.cafetito.exception.SinPrivilegiosException;
import com.cafetito.exception.UsuarioInactivoException;
import com.cafetito.repository.UsuarioRepository;
import com.cafetito.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String MSG_CREDENCIALES = "Error al autenticar. Verifique usuario y contrasena.";

    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CredencialesInvalidasException(MSG_CREDENCIALES));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new UsuarioInactivoException("El usuario esta inactivo. No se permite el ingreso.");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new CredencialesInvalidasException(MSG_CREDENCIALES);
        }

        Rol rol = usuario.getRol();
        if (rol == null) {
            throw new SinPrivilegiosException();
        }

        String nitAgricultor = rol == Rol.ROLE_AGRICULTOR ? usuario.getNitAgricultor() : null;

        // ✅ CORRECCIÓN: Ahora pasamos el usuario.getId() como primer parámetro
        // Antes: jwtUtil.generateToken(usuario.getUsername(), rol, nitAgricultor);
        String token = jwtUtil.generateToken(
                usuario.getId(),
                usuario.getUsername(),
                rol,
                nitAgricultor
        );

        return new LoginResponseDTO(token, rol, usuario.getUsername(), nitAgricultor);
    }
}
