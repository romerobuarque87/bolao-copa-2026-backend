package com.bolao.copa2026.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BolaoResponseDTO {

    private Long id;

    private String nome;

    private String codigoConvite;

    private Boolean ativo;

    private LocalDateTime dataCriacao;

    private Long organizadorId;

    private String organizadorNome;
}