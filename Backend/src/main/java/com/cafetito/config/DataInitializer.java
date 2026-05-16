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
            // Eliminamos la validación global de "count > 0".
            // Ahora el sistema intentará crear todos los usuarios definidos abajo,
            // pero el método "crearUsuario" evitará los duplicados.

            // Agricultores
            crearUsuario("agricultor", "agricultor123", Rol.ROLE_AGRICULTOR, "NIT-AGR-001");
            crearUsuario("Juan Perez", "juan123", Rol.ROLE_AGRICULTOR, "NIT-AGR-002");
            crearUsuario("Maria Lopez", "maria123", Rol.ROLE_AGRICULTOR, "NIT-AGR-003");
            crearUsuario("Carlos Ruiz", "carlos123", Rol.ROLE_AGRICULTOR, "NIT-AGR-004");

            // Personal del Beneficio
            crearUsuario("administrador", "admin123", Rol.ROLE_BENEFICIO, null);
            crearUsuario("pesaje", "pesaje123", Rol.ROLE_PESOCABAL, null);

            System.out.println("[DataInitializer] Verificación de usuarios de prueba completada.");
        } catch (Exception e) {
            System.err.println("[DataInitializer] Error verificando/creando usuarios de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearUsuario(String username, String password, Rol rol, String nitAgricultor) {
        // Esta es la validación que realmente importa. Solo lo crea si el username no existe.
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode(password));
            u.setActivo(true);
            u.setRol(rol);
            u.setNitAgricultor(nitAgricultor);
            usuarioRepository.save(u);
            System.out.println("   -> Creado nuevo usuario: " + username);
        }
    }
}