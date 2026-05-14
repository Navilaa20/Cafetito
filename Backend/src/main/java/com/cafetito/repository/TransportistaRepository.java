package com.cafetito.repository;

import com.cafetito.entity.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransportistaRepository extends JpaRepository<Transportista, Long> {

    boolean existsByCui(String cui);


    // ✅ Cambiamos NitAgricultor por IdUsuario
    List<Transportista> findByIdUsuario(Long idUsuario);

    List<Transportista> findByCuiContainingIgnoreCase(String cui);

    List<Transportista> findByEstado(Boolean estado);

    // ✅ Cambiamos NitAgricultor por IdUsuario
    List<Transportista> findByIdUsuarioAndEstadoTrue(Long idUsuario);

    @Query("SELECT COUNT(t) FROM Transportista t WHERE t.idUsuario = :idAgricultor")
    long countByIdAgricultor(@Param("idAgricultor") Long idAgricultor);
}