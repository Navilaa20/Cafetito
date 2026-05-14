package com.cafetito.repository;

import com.cafetito.entity.Boleta;
import com.cafetito.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
}
