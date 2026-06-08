package com.bolao.copa2026.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipanteBolaoResponseDTO {

    private Long id;
    private Long usuarioId;
    private String nomeUsuario;
    private Long bolaoId;
    private String nomeBolao;
    private String codigoConvite;
    private Integer pontos;
    private Boolean palpitesEnviados;
}