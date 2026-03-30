package com.bolsaempleo.service;

import com.bolsaempleo.model.Caracteristica;
import com.bolsaempleo.model.Empresa;
import com.bolsaempleo.model.Puesto;
import com.bolsaempleo.model.RequisitoPuesto;
import com.bolsaempleo.repository.CaracteristicaRepository;
import com.bolsaempleo.repository.PuestoRepository;
import com.bolsaempleo.repository.RequisitoPuestoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PuestoService {

    private final PuestoRepository puestoRepository;
    private final RequisitoPuestoRepository requisitoPuestoRepository;
    private final CaracteristicaRepository caracteristicaRepository;

    public List<Puesto> obtenerUltimosPublicos() {
        return puestoRepository.findTop5ByEsPublicoTrueAndActivoTrueOrderByFechaRegistroDesc();
    }

    public List<Puesto> buscarPublicos(List<Long> caracteristicaIds) {
        if (caracteristicaIds == null || caracteristicaIds.isEmpty()) {
            return puestoRepository.findByEsPublicoTrueAndActivoTrue();
        }
        return puestoRepository.findPublicosActivosByCaracteristicas(caracteristicaIds);
    }

    public List<Puesto> buscarParaOferente(List<Long> caracteristicaIds) {
        if (caracteristicaIds == null || caracteristicaIds.isEmpty()) {
            return puestoRepository.findByActivoTrue();
        }
        return puestoRepository.findActivosByCaracteristicas(caracteristicaIds);
    }

    public List<Puesto> obtenerPorEmpresa(Long empresaId) {
        return puestoRepository.findByEmpresaIdOrderByFechaRegistroDesc(empresaId);
    }

    public Optional<Puesto> findById(Long id) {
        return puestoRepository.findById(id);
    }

    @Transactional
    public Puesto publicar(Empresa empresa,
                           String descripcion,
                           BigDecimal salarioOfrecido,
                           boolean esPublico,
                           List<Long> caracteristicaIds,
                           List<Integer> nivelesRequeridos) {

        Puesto puesto = new Puesto();
        puesto.setEmpresa(empresa);
        puesto.setDescripcion(descripcion);
        puesto.setSalarioOfrecido(salarioOfrecido);
        puesto.setEsPublico(esPublico);
        puesto.setActivo(true);
        puesto = puestoRepository.save(puesto);

        agregarRequisitos(puesto, caracteristicaIds, nivelesRequeridos);
        return puesto;
    }

    @Transactional
    public Puesto actualizar(Long puestoId,
                             String descripcion,
                             BigDecimal salarioOfrecido,
                             boolean esPublico,
                             List<Long> caracteristicaIds,
                             List<Integer> nivelesRequeridos) {

        Puesto puesto = puestoRepository.findById(puestoId)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado: " + puestoId));

        puesto.setDescripcion(descripcion);
        puesto.setSalarioOfrecido(salarioOfrecido);
        puesto.setEsPublico(esPublico);

        requisitoPuestoRepository.deleteByPuestoId(puestoId);
        puesto.getRequisitos().clear();
        puestoRepository.save(puesto);

        agregarRequisitos(puesto, caracteristicaIds, nivelesRequeridos);
        return puesto;
    }

    @Transactional
    public void desactivar(Long puestoId) {
        Puesto puesto = puestoRepository.findById(puestoId)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado: " + puestoId));
        puesto.setActivo(false);
        puestoRepository.save(puesto);
    }

    public List<Puesto> obtenerPorMes(int anio, int mes) {
        LocalDateTime inicio = LocalDateTime.of(anio, mes, 1, 0, 0);
        LocalDateTime fin = inicio.plusMonths(1);
        return puestoRepository.findByMes(inicio, fin);
    }

    private void agregarRequisitos(Puesto puesto, List<Long> caracteristicaIds, List<Integer> niveles) {
        if (caracteristicaIds == null) return;
        for (int i = 0; i < caracteristicaIds.size(); i++) {
            Long cid = caracteristicaIds.get(i);
            if (cid == null) continue;
            int nivel = (niveles != null && i < niveles.size() && niveles.get(i) != null)
                    ? niveles.get(i) : 1;

            Caracteristica c = caracteristicaRepository.findById(cid)
                    .orElseThrow(() -> new IllegalArgumentException("Característica no encontrada: " + cid));

            RequisitoPuesto req = new RequisitoPuesto();
            req.setPuesto(puesto);
            req.setCaracteristica(c);
            req.setNivelRequerido(nivel);
            requisitoPuestoRepository.save(req);
        }
    }
}
