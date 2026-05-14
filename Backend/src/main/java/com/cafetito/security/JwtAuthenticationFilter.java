package com.cafetito.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 1. Validar que el header exista y tenga el formato Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // 2. Validar integridad del token
        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer información del Token
        String username = jwtUtil.extractUsername(token);
        String rol = jwtUtil.extractRol(token);
        String nitAgricultor = jwtUtil.extractNitAgricultor(token);
        Long idUsuario = jwtUtil.extractIdUsuario(token); // ✅ Extraemos el ID que agregamos a JwtUtil

        // 4. Normalizar el rol para Spring Security
        if (rol != null && !rol.startsWith("ROLE_")) {
            rol = "ROLE_" + rol;
        }

        // 5. Configurar la autenticación en el contexto de Spring
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var authorities = List.of(new SimpleGrantedAuthority(rol));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // ✅ MODIFICACIÓN CRÍTICA:
            // Creamos un mapa que contenga tanto el NIT como el ID de usuario.
            // Esto es lo que lee tu controlador con getIdUsuarioFromAuth()
            Map<String, Object> extraDetails = new HashMap<>();
            extraDetails.put("idUsuario", idUsuario);

            if (nitAgricultor != null && !nitAgricultor.isBlank()) {
                extraDetails.put("nitAgricultor", nitAgricultor);
            }

            authentication.setDetails(extraDetails);

            // Inyectar en el contexto para que el Controlador pueda verlo
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}