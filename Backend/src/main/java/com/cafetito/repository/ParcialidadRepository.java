package com.cafetito.repository;

import com.cafetito.entity.Parcialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParcialidadRepository extends JpaRepository<Parcialidad, Long> {

    // Usamos una consulta JPQL explícita para evitar errores de interpretación
    @Query("SELECT p FROM Parcialidad p WHERE p.idCuenta.idCuenta = :idCuenta")
    List<Parcialidad> buscarPorIdCuenta(@Param("idCuenta") Long idCuenta);

    // Este lo dejamos para el módulo de Agricultor
    List<Parcialidad> findByIdPesaje(Long idPesaje);

    long countByIdPesaje(Long idPesaje);
}