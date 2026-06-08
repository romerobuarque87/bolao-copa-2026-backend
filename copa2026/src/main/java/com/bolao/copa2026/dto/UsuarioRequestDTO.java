package com.bolao.copa2026.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequestDTO {

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private Boolean receberNotificacaoEmail;
    private Boolean receberNotificacaoWhatsapp;
    private Boolean administrador;
}