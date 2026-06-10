package com.bolao.copa2026.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PalpiteResponseDTO {

    private Long id;

    private Long participanteBolaoId;
    private String nomeUsuario;
    private Long bolaoId;
    private String nomeBolao;

    private Long jogoId;
    private String timeCasaNome;
    private String timeCasaSigla;
    private String timeVisitanteNome;
    private String timeVisitanteSigla;
    private LocalDateTime dataHoraJogo;
    private String fase;

    private Integer golsCasaPalpite;
    private Integer golsVisitantePalpite;

    private Long classificadoPalpiteId;
    private String classificadoPalpiteNome;
    private String classificadoPalpiteSigla;

    private Integer pontosObtidos;
}