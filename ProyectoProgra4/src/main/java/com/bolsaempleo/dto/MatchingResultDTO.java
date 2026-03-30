package com.bolsaempleo.dto;

import com.bolsaempleo.model.Oferente;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchingResultDTO {

    private final Oferente oferente;

    private final int requisitosCumplidos;

    private final int totalRequisitos;

    public double getPorcentajeCoincidencia() {
        if (totalRequisitos == 0) return 0.0;
        return (requisitosCumplidos * 100.0) / totalRequisitos;
    }

    public String getPorcentajeFormateado() {
        return String.format("%.2f%%", getPorcentajeCoincidencia());
    }

    public String getRequisitosFormateados() {
        return requisitosCumplidos + " / " + totalRequisitos;
    }
}
