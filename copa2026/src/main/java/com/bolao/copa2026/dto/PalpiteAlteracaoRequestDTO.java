package com.bolao.copa2026.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PalpiteAlteracaoRequestDTO {

    private Integer golsCasaPalpite;
    private Integer golsVisitantePalpite;
    private Long classificadoPalpiteId;
}
