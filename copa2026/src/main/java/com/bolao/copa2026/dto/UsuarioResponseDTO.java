package com.bolao.copa2026.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private Boolean receberNotificacaoEmail;
    private Boolean receberNotificacaoWhatsapp;
    private Boolean administrador;
    private Boolean ativo;
    private LocalDateTime dataCadastro;

    public UsuarioResponseDTO(
            Long id,
            String nome,
            String email,
            String telefone,
            Boolean receberNotificacaoEmail,
            Boolean receberNotificacaoWhatsapp,
            Boolean administrador,
            LocalDateTime dataCadastro
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.receberNotificacaoEmail = receberNotificacaoEmail;
        this.receberNotificacaoWhatsapp = receberNotificacaoWhatsapp;
        this.administrador = administrador;
        this.dataCadastro = dataCadastro;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Boolean getReceberNotificacaoEmail() {
        return receberNotificacaoEmail;
    }

    public Boolean getReceberNotificacaoWhatsapp() {
        return receberNotificacaoWhatsapp;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}