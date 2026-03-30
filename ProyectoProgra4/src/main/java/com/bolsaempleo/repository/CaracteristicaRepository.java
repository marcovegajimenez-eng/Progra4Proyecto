package com.bolsaempleo.repository;

import com.bolsaempleo.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {

    List<Caracteristica> findByPadreIsNullOrderByNombreAsc();

    List<Caracteristica> findByPadreIdOrderByNombreAsc(Long padreId);

    List<Caracteristica> findAllByOrderByNombreAsc();

    @Query("SELECT c FROM Caracteristica c WHERE c.id NOT IN " +
           "(SELECT DISTINCT child.padre.id FROM Caracteristica child WHERE child.padre IS NOT NULL)")
    List<Caracteristica> findHojas();
}
