package com.bolao.copa2026.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarUsuarioLogadoRequestDTO {

    private String nome;
    private String telefone;
    private Boolean receberNotificacaoEmail;
    private Boolean receberNotificacaoWhatsapp;
}