package com.cafetito.config;

import com.cafetito.entity.Rol;
import com.cafetito.entity.Usuario;
import com.cafetito.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            long count = usuarioRepository.count();
            if (count > 0) {
                System.out.println("[DataInitializer] Ya existen " + count + " usuarios, omitiendo creacion.");
                return;
            }
            crearUsuario("agricultor", "agricultor123", Rol.ROLE_AGRICULTOR, "NIT-AGR-001");
            crearUsuario("administrador", "admin123", Rol.ROLE_BENEFICIO, null);
            crearUsuario("pesaje", "pesaje123", Rol.ROLE_PESOCABAL, null);
            System.out.println("[DataInitializer] Usuarios de prueba creados: agricultor, administrador, pesaje");
        } catch (Exception e) {
            System.err.println("[DataInitializer] Error creando usuarios de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearUsuario(String username, String password, Rol rol, String nitAgricultor) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode(password));
            u.setActivo(true);
            u.setRol(rol);
            u.setNitAgricultor(nitAgricultor);
            usuarioRepository.save(u);
        }
    }
}
