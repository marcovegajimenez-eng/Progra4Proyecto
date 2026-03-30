package com.bolsaempleo.controller;

import com.bolsaempleo.model.Caracteristica;
import com.bolsaempleo.model.Puesto;
import com.bolsaempleo.service.CaracteristicaService;
import com.bolsaempleo.service.PuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/puestos")
@RequiredArgsConstructor
public class PuestoPublicoController {

    private final PuestoService puestoService;
    private final CaracteristicaService caracteristicaService;

    @GetMapping("/buscar")
    public String formBuscar(Model model) {
        model.addAttribute("raices", caracteristicaService.obtenerRaices());
        return "public/buscar-puestos";
    }

    @PostMapping("/buscar")
    public String buscar(@RequestParam(value = "caracteristicaIds", required = false)
                         List<Long> caracteristicaIds,
                         Model model) {
        List<Puesto> resultados = puestoService.buscarPublicos(caracteristicaIds);
        model.addAttribute("raices", caracteristicaService.obtenerRaices());
        model.addAttribute("resultados", resultados);
        model.addAttribute("seleccionados", caracteristicaIds);
        return "public/buscar-puestos";
    }
}
