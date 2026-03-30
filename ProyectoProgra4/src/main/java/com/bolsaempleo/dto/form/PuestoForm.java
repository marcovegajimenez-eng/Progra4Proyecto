package com.bolsaempleo.dto.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PuestoForm {

    @NotBlank(message = "La descripción del puesto es obligatoria")
    private String descripcion;

    private BigDecimal salarioOfrecido;

    private boolean esPublico = true;

    private Map<Long, Integer> niveles = new HashMap<>();
}
