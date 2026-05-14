package com.cafetito.repository;

import com.cafetito.entity.Linea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LineaRepository extends JpaRepository<Linea, Integer> {

    // ✅ Este permitirá que cuando elijas 'Volvo' (ID 1),
    // el backend solo devuelva las líneas asociadas a ese ID de marca.
    List<Linea> findByIdMarca(Integer idMarca);
}