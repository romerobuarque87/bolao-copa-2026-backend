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
public class ParticipanteBolao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Bolao bolao;

    private Integer pontos = 0;

    private LocalDateTime dataEntrada = LocalDateTime.now();

    private Boolean ativo = true;

    private Boolean palpitesEnviados = false;

    private Boolean alteracaoLiberadaPeloAdmin = false;
}