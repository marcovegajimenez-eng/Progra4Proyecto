package com.bolsaempleo.config;

import com.bolsaempleo.model.Administrador;
import com.bolsaempleo.model.Caracteristica;
import com.bolsaempleo.repository.AdministradorRepository;
import com.bolsaempleo.repository.CaracteristicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdministradorRepository adminRepo;
    private final CaracteristicaRepository caracteristicaRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCaracteristicas();
    }

    private void seedAdmin() {
        if (adminRepo.findByIdentificacion("admin").isEmpty()) {
            Administrador a = new Administrador();
            a.setIdentificacion("admin");
            a.setNombre("Administrador del Sistema");
            a.setClave(passwordEncoder.encode("admin1234"));
            adminRepo.save(a);
        }
    }

    private void seedCaracteristicas() {
        if (caracteristicaRepo.count() > 0) return;

        Caracteristica bd      = crearRaiz("Bases de Datos");
        Caracteristica ciber   = crearRaiz("Ciberseguridad");
        Caracteristica langs   = crearRaiz("Lenguajes de programación");
        Caracteristica web     = crearRaiz("Tecnologías Web");
        Caracteristica testing = crearRaiz("Testing");
        Caracteristica model   = crearRaiz("Modelado");

        Caracteristica motores = crearHijo("Motores", bd);
        crearHijo("MySQL",     motores);
        crearHijo("Oracle",    motores);
        crearHijo("PostgreSQL",motores);
        crearHijo("SQL Server",motores);

        crearHijo("C#",     langs);
        crearHijo("Java",   langs);
        crearHijo("Kotlin", langs);
        crearHijo("Python", langs);

        crearHijo("HTML",        web);
        crearHijo("CSS",         web);
        crearHijo("JavaScript",  web);
        crearHijo("Spring Boot", web);
        crearHijo("Thymeleaf",   web);

        Caracteristica junit = crearHijo("JUnit", testing);
        crearHijo("Assertions", junit);
        crearHijo("Test cases", junit);

        crearHijo("OWASP",      ciber);
        crearHijo("Pentesting", ciber);

        crearHijo("UML", model);

        System.out.println("[DataInitializer] Características sembradas correctamente.");
    }

    private Caracteristica crearRaiz(String nombre) {
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre);
        return caracteristicaRepo.save(c);
    }

    private Caracteristica crearHijo(String nombre, Caracteristica padre) {
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre);
        c.setPadre(padre);
        return caracteristicaRepo.save(c);
    }
}
