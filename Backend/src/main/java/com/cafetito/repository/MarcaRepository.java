package com.cafetito.repository;

import com.cafetito.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    // El ID es Integer porque en tu SQL es 'serial'
}