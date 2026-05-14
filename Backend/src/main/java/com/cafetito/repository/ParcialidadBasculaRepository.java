package com.cafetito.repository;

import com.cafetito.entity.Cuenta;
import com.cafetito.entity.ParcialidadBascula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcialidadBasculaRepository extends JpaRepository<ParcialidadBascula, Long> {
}
