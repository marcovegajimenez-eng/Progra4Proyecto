package com.bolsaempleo.service;

import com.bolsaempleo.dto.MatchingResultDTO;
import com.bolsaempleo.model.HabilidadOferente;
import com.bolsaempleo.model.Oferente;
import com.bolsaempleo.model.Puesto;
import com.bolsaempleo.model.RequisitoPuesto;
import com.bolsaempleo.repository.HabilidadOferenteRepository;
import com.bolsaempleo.repository.OferenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final OferenteRepository oferenteRepository;
    private final HabilidadOferenteRepository habilidadRepository;

    public List<MatchingResultDTO> buscarCandidatos(Puesto puesto) {
        List<RequisitoPuesto> requisitos = puesto.getRequisitos();
        int totalRequisitos = requisitos.size();

        List<Oferente> aprobados = oferenteRepository.findByAprobadoTrue();

        return aprobados.stream()
                .map(oferente -> calcularMatch(oferente, requisitos, totalRequisitos))
                .filter(result -> result.getRequisitosCumplidos() > 0)
                .sorted(Comparator.comparingDouble(MatchingResultDTO::getPorcentajeCoincidencia).reversed())
                .collect(Collectors.toList());
    }

    private MatchingResultDTO calcularMatch(Oferente oferente,
                                            List<RequisitoPuesto> requisitos,
                                            int totalRequisitos) {

        List<HabilidadOferente> habilidades = habilidadRepository.findByOferenteId(oferente.getId());
        Map<Long, Integer> nivelPorCaracteristica = habilidades.stream()
                .collect(Collectors.toMap(
                        h -> h.getCaracteristica().getId(),
                        HabilidadOferente::getNivel
                ));

        int cumplidos = 0;
        for (RequisitoPuesto req : requisitos) {
            Long cid = req.getCaracteristica().getId();
            Integer nivelOferente = nivelPorCaracteristica.get(cid);
            if (nivelOferente != null && nivelOferente >= req.getNivelRequerido()) {
                cumplidos++;
            }
        }

        return new MatchingResultDTO(oferente, cumplidos, totalRequisitos);
    }
}
