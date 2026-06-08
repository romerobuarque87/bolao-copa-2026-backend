package com.bolao.copa2026.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private Long usuarioId;
    private String nome;
    private String email;
    private Boolean administrador;
}