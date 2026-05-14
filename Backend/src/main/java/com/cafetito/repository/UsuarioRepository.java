package com.cafetito.repository;

import com.cafetito.entity.Rol;
import com.cafetito.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByRol(Rol rol);
    // Cumple con el FA01: Busca por Rol y caracteres que coincidan con el NIT
    List<Usuario> findByRolAndNitAgricultorContainingIgnoreCase(Rol rol, String nit);

    Optional<Usuario> findByNitAgricultor(String nitAgricultor);

    Optional<Usuario> findByUsername(String username);

}
