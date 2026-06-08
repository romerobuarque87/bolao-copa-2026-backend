package com.bolao.copa2026.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioLogadoResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private Boolean administrador;
}