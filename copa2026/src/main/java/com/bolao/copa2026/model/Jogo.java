package com.bolao.copa2026.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    private Integer penaltisCasa;

    private Integer penaltisVisitante;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private FaseCopa fase;

    private String grupo;

    private Boolean finalizado = false;

    private Integer ordemMataMata;
}
