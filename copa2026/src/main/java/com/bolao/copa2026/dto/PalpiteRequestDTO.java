package com.bolao.copa2026.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PalpiteRequestDTO {

    private Long participanteBolaoId;
    private Long jogoId;
    private Integer golsCasaPalpite;
    private Integer golsVisitantePalpite;
}