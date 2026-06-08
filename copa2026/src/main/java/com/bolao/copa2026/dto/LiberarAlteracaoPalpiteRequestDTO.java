package com.bolao.copa2026.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiberarAlteracaoPalpiteRequestDTO {

    private Long administradorId;
    private Long palpiteId;
    private String motivo;
}