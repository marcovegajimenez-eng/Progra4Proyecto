package com.bolsaempleo.repository;

import com.bolsaempleo.model.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Long> {

    List<Puesto> findTop5ByEsPublicoTrueAndActivoTrueOrderByFechaRegistroDesc();

    List<Puesto> findByEsPublicoTrueAndActivoTrue();

    List<Puesto> findByActivoTrue();

    List<Puesto> findByEmpresaIdOrderByFechaRegistroDesc(Long empresaId);

    @Query("SELECT DISTINCT p FROM Puesto p JOIN p.requisitos r " +
           "WHERE p.esPublico = true AND p.activo = true " +
           "AND r.caracteristica.id IN :ids")
    List<Puesto> findPublicosActivosByCaracteristicas(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT p FROM Puesto p JOIN p.requisitos r " +
           "WHERE p.activo = true AND r.caracteristica.id IN :ids")
    List<Puesto> findActivosByCaracteristicas(@Param("ids") List<Long> ids);

    @Query("SELECT p FROM Puesto p WHERE p.fechaRegistro >= :inicio AND p.fechaRegistro < :fin " +
           "ORDER BY p.fechaRegistro ASC")
    List<Puesto> findByMes(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
