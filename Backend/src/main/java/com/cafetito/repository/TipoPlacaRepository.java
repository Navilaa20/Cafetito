package com.cafetito.repository;

import com.cafetito.entity.Pesaje;
import com.cafetito.entity.TipoPlaca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoPlacaRepository extends JpaRepository<TipoPlaca, Integer> {


}