package com.cafetito.repository;

import com.cafetito.entity.MedidaPeso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaPesoRepository extends JpaRepository<MedidaPeso, Long> {
}