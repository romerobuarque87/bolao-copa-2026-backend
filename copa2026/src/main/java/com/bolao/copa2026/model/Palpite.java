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
public class Palpite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ParticipanteBolao participanteBolao;

    @ManyToOne
    private Jogo jogo;

    private Integer golsCasaPalpite;

    private Integer golsVisitantePalpite;

    private Integer pontosObtidos = 0;

    private Boolean alteracaoLiberadaPeloAdmin = false;

    @ManyToOne
    private Usuario administradorQueLiberou;

    private LocalDateTime dataLiberacaoAlteracao;

    private String motivoLiberacaoAlteracao;
}