package com.bolao.copa2026.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JogoRequestDTO {

    private Long timeCasaId;
    private Long timeVisitanteId;
    private Long estadioId;
    private LocalDateTime dataHora;
    private String fase;
}