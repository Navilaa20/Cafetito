package com.cafetito.repository;

import com.cafetito.entity.Transporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransporteRepository extends JpaRepository<Transporte, Long> { // ✅ Cambiado de String a Long

    List<Transporte> findByIdUsuario(Long idUsuario);

    boolean existsByPlaca(String placa);

    @Query("SELECT COUNT(t) FROM Transporte t WHERE t.idUsuario = :idAgricultor")
    long countByIdAgricultor(@Param("idAgricultor") Long idAgricultor);

    List<Transporte> findByPlacaContainingIgnoreCase(String placa);

    // ✅ Spring ahora reconocerá estos métodos porque el ID del Repo coincide con el @Id de la Entityd
    List<Transporte> findByActivo(Boolean activo);


    // ✅ Cambiado a idUsuario
    List<Transporte> findByIdUsuarioAndActivoTrue(Long idUsuario);
}