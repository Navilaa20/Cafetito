package com.cafetito.repository;

import com.cafetito.entity.Cuenta;
import com.cafetito.entity.EstadoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByNitAgricultor(String nitAgricultor);
    List<Cuenta> findByEstadoCuenta(EstadoCuenta estado);
    List<Cuenta> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT COUNT(c) FROM Cuenta c WHERE c.idAgricultor = :idAgricultor")
    long countByIdAgricultor(@Param("idAgricultor") Long idAgricultor);

    // ✅ Necesario para validar antes de crear cuenta desde pesaje
    boolean existsByIdPesaje(Long idPesaje);
}