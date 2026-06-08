package com.bolao.copa2026.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingResponseDTO {

    private Integer posicao;
    private Long participanteBolaoId;
    private Long usuarioId;
    private String nomeUsuario;
    private Long bolaoId;
    private String nomeBolao;
    private Integer pontos;
}