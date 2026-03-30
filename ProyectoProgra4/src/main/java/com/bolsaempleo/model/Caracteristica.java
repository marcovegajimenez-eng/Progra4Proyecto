package com.bolsaempleo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caracteristica")
@Getter
@Setter
@NoArgsConstructor
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    private Caracteristica padre;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Caracteristica> hijos = new ArrayList<>();

    @Transient
    public boolean isHoja() {
        return hijos == null || hijos.isEmpty();
    }

    public String getRuta() {
        if (padre == null) {
            return nombre;
        }
        return padre.getRuta() + " / " + nombre;
    }
}
