package com.bolsaempleo.controller;

import com.bolsaempleo.model.Oferente;
import com.bolsaempleo.security.CustomUserDetails;
import com.bolsaempleo.service.CaracteristicaService;
import com.bolsaempleo.service.CvStorageService;
import com.bolsaempleo.service.OferenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/oferente")
@RequiredArgsConstructor
public class OferenteController {

    private final OferenteService       oferenteService;
    private final CaracteristicaService caracteristicaService;
    private final CvStorageService      cvStorageService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        Oferente oferente = getOferente(user);
        model.addAttribute("oferente", oferente);
        return "oferente/dashboard";
    }

    @GetMapping("/habilidades")
    public String misHabilidades(@AuthenticationPrincipal CustomUserDetails user,
                                  @RequestParam(value = "actualid", required = false) Long actualid,
                                  Model model) {
        Long oferenteId = user.getId();
        model.addAttribute("oferente", getOferente(user));
        model.addAttribute("habilidades", oferenteService.obtenerHabilidades(oferenteId));

        if (actualid == null) {
            model.addAttribute("hijos", caracteristicaService.obtenerRaices());
            model.addAttribute("actual", null);
        } else {
            var actual = caracteristicaService.findById(actualid).orElse(null);
            model.addAttribute("actual", actual);
            model.addAttribute("hijos", caracteristicaService.obtenerHijos(actualid));
        }
        return "oferente/habilidades";
    }

    @PostMapping("/habilidades/agregar")
    public String agregarHabilidad(@AuthenticationPrincipal CustomUserDetails user,
                                    @RequestParam("caracteristicaId") Long caracteristicaId,
                                    @RequestParam("nivel") int nivel,
                                    RedirectAttributes ra) {
        if (nivel < 1 || nivel > 5) {
            ra.addFlashAttribute("error", "El nivel debe estar entre 1 y 5.");
            return "redirect:/oferente/habilidades";
        }
        try {
            oferenteService.guardarHabilidad(user.getId(), caracteristicaId, nivel);
            ra.addFlashAttribute("success", "Habilidad guardada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/oferente/habilidades";
    }

    @PostMapping("/habilidades/{caracteristicaId}/eliminar")
    public String eliminarHabilidad(@AuthenticationPrincipal CustomUserDetails user,
                                     @PathVariable Long caracteristicaId,
                                     RedirectAttributes ra) {
        try {
            oferenteService.eliminarHabilidad(user.getId(), caracteristicaId);
            ra.addFlashAttribute("success", "Habilidad eliminada.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/oferente/habilidades";
    }

    @GetMapping("/cv")
    public String verCv(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("oferente", getOferente(user));
        return "oferente/cv";
    }

    @PostMapping("/cv/subir")
    public String subirCv(@AuthenticationPrincipal CustomUserDetails user,
                           @RequestParam("cvFile") MultipartFile file,
                           RedirectAttributes ra) {
        try {
            String path = cvStorageService.store(file, user.getId());
            oferenteService.actualizarCv(user.getId(), path);
            ra.addFlashAttribute("success", "CV subido correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al subir el CV: " + e.getMessage());
        }
        return "redirect:/oferente/cv";
    }

    private Oferente getOferente(CustomUserDetails user) {
        return oferenteService.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("Oferente no encontrado"));
    }
}
