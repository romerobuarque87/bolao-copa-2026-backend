package com.bolao.copa2026.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassificacaoGrupoDTO {

    private Integer posicao;

    private Long selecaoId;
    private String nomeSelecao;
    private String siglaFifa;
    private String bandeiraUrl;
    private String grupo;

    private Integer jogos = 0;
    private Integer vitorias = 0;
    private Integer empates = 0;
    private Integer derrotas = 0;

    private Integer golsPro = 0;
    private Integer golsContra = 0;
    private Integer saldoGols = 0;
    private Integer pontos = 0;
}