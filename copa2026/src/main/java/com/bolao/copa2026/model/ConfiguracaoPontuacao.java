package com.bolao.copa2026.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ConfiguracaoPontuacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer pontosPlacarExato = 10;

    private Integer pontosResultado = 5;

    private Integer pontosGolsMandante = 2;

    private Integer pontosGolsVisitante = 2;

    private Integer pontosClassificado = 5;
}