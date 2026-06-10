package com.bolao.copa2026.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JogoResponseDTO {

    private Long id;

    private Long timeCasaId;
    private String timeCasaNome;
    private String timeCasaSigla;
    private String timeCasaBandeiraUrl;

    private Long timeVisitanteId;
    private String timeVisitanteNome;
    private String timeVisitanteSigla;
    private String timeVisitanteBandeiraUrl;

    private Long estadioId;
    private String estadioNome;
    private String estadioCidade;
    private String estadioPais;

    private Integer golsCasa;
    private Integer golsVisitante;

    private Integer penaltisCasa;
    private Integer penaltisVisitante;

    private LocalDateTime dataHora;

    private String fase;

    private String grupo;

    private Boolean finalizado;
}