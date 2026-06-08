package com.bolao.copa2026.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Selecao timeCasa;

    @ManyToOne
    private Selecao timeVisitante;

    @ManyToOne
    private Estadio estadio;

    private Integer golsCasa;

    private Integer golsVisitante;

    private LocalDateTime dataHora;

    private String fase;

    private Boolean finalizado = false;
}