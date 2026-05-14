package com.cafetito.repository;

import com.cafetito.entity.Pesaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PesajeRepository extends JpaRepository<Pesaje, Long> {

    List<Pesaje> findByIdAgricultor(Long idAgricultor);

    @Query(value = "SELECT agricultores.convertir_a_kg(:cantidad, :idMedida, :esquema)", nativeQuery = true)
    Double calcularConversion(@Param("cantidad") Double cantidad,
                              @Param("idMedida") Long idMedida,
                              @Param("esquema") String esquema);

    List<Pesaje> findByEstado(String estado);

}
