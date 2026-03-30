package com.bolsaempleo.service;

import com.bolsaempleo.model.Caracteristica;
import com.bolsaempleo.repository.CaracteristicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CaracteristicaService {

    private final CaracteristicaRepository caracteristicaRepository;

    public List<Caracteristica> obtenerRaices() {
        return caracteristicaRepository.findByPadreIsNullOrderByNombreAsc();
    }

    public List<Caracteristica> obtenerHijos(Long padreId) {
        return caracteristicaRepository.findByPadreIdOrderByNombreAsc(padreId);
    }

    public Optional<Caracteristica> findById(Long id) {
        return caracteristicaRepository.findById(id);
    }

    public List<Caracteristica> findAll() {
        return caracteristicaRepository.findAllByOrderByNombreAsc();
    }

    public List<Caracteristica> obtenerHojas() {
        return caracteristicaRepository.findHojas();
    }

    @Transactional
    public Caracteristica crear(String nombre, Long padreId) {
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre.trim());
        if (padreId != null) {
            Caracteristica padre = caracteristicaRepository.findById(padreId)
                    .orElseThrow(() -> new IllegalArgumentException("Padre no encontrado: " + padreId));
            c.setPadre(padre);
        }
        return caracteristicaRepository.save(c);
    }

    @Transactional
    public void eliminar(Long id) {
        caracteristicaRepository.deleteById(id);
    }
}
